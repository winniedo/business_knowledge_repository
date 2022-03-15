package com.upb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import org.bson.Document;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.nio.ExportException;
import org.jgrapht.traverse.DepthFirstIterator;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;
import de.linguatools.disco.TextSimilarity;

/**
 * Hello world!
 */
public final class App {
	private App() {
	}

	final static double semanticSimilarityThreshold = 0.7;
	static DISCO disco;

	/**
	 * The starting point for the demo.
	 *
	 * @param args ignored.
	 *
	 * @throws ExportException            if graph cannot be exported.
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CorruptConfigFileException
	 */
	public static void main(String[] args)
			throws ExportException, IOException, InterruptedException, CorruptConfigFileException {

		disco = DISCO.load("/home/kuosi/dev/enwiki-20130403-word2vec-lm-mwl-lc-sim.denseMatrix");
		// createDMNfromStandard();
		Graph<DMNVertex, DMNEdge> dmn = loadFromDB("alphaBank");
		// saveToDB("alphaBank", dmn);

		// note undirected edges are printed as: {<v1>,<v2>}
		/*
		 * System.out.println("-- toString output");
		 * System.out.println(dmn.toString());
		 * System.out.println();
		 */

		System.out.println(dmn.vertexSet().size());

		CycleDetector cycleDetector​ = new CycleDetector(dmn);
		boolean cycle = cycleDetector​.detectCycles();
		System.out.println(cycle);

		// printDMN(dmn);

		//searchMatchingOutputs("Pre-bureau affordability", dmn);

	}

	public static void printDMN(Graph<DMNVertex, DMNEdge> dmn) {
		// Print out the graph to be sure it's really complete
		Iterator<DMNVertex> iter = new DepthFirstIterator<>(dmn);
		while (iter.hasNext()) {
			DMNVertex vertex = iter.next();
			System.out
					.println(
							"Vertex " + vertex + " is connected to: "
									+ dmn.edgesOf(vertex).toString());
		}
	}

	public static float checkSimilarity(String text1, String text2)
			throws IOException, InterruptedException, CorruptConfigFileException {
		float ts = TextSimilarity.textSimilarity(text1, text2, disco, DISCO.SimilarityMeasure.COSINE);
		System.out.println(ts);
		return ts;
	}

	public static List<DMNVertex> searchMatchingOutput(String userOutput, Graph<DMNVertex, DMNEdge> dmn)
			throws IOException, InterruptedException, CorruptConfigFileException {
		List<DMNVertex> foundVertexOutputs = new ArrayList<DMNVertex>();
		for (DMNVertex vertexOutput : dmn.vertexSet()) {
			if (vertexOutput.getVertexType().equals(DMNVertexType.OutputData)) {
				if (checkSimilarity(userOutput, vertexOutput.getVertexName()) >= semanticSimilarityThreshold) {
					foundVertexOutputs.add(vertexOutput);
				}
			}
		}
		return foundVertexOutputs;
	}

	public static List<DMNVertex> searchMatchingInputs(List<String> userIntputs, Graph<DMNVertex, DMNEdge> dmn)
			throws IOException, InterruptedException, CorruptConfigFileException {
		List<DMNVertex> foundVertexInputs = new ArrayList<DMNVertex>();
		for (DMNVertex vertexInput : dmn.vertexSet()) {
			boolean found = false;
			int i = 0;
			while ((! found) && i < userIntputs.size()) {
				String userIntput = userIntputs.get(i);
				if (checkSimilarity(userIntput, vertexInput.getVertexName()) >= semanticSimilarityThreshold) {
					foundVertexInputs.add(vertexInput);
					found = true;
				}
				i++;
			}
		}
		return foundVertexInputs;
	}

	public static List<Graph<DMNVertex, DMNEdge>> generateAllGraphsBasedOnDirectedPaths(Graph<DMNVertex, DMNEdge> dmn, Set<DMNVertex> vertexInputs, Set<DMNVertex> vertexOutputs) {
		AllDirectedPaths<DMNVertex, DMNEdge> allDirectedPaths = new AllDirectedPaths<DMNVertex, DMNEdge>(dmn);
		List<GraphPath<DMNVertex, DMNEdge>> pathsSubSet = allDirectedPaths.getAllPaths(vertexInputs, vertexOutputs, false, null);

		Set<DMNVertex> vertexSet = new HashSet<DMNVertex>();

		//Get all path from start to end

		for (GraphPath<DMNVertex, DMNEdge> path: pathsSubSet){
			vertexSet.addAll(path.getVertexList());			
		}

		//Calculate power set

		Set<Set<DMNVertex>> powerSet = Sets.powerSet(vertexInputs);

		//Remove all set that do not contain one end output vertex or do not contain at least one input vertex

		Set<Set<DMNVertex>> newPowerSet = new HashSet<Set<DMNVertex>>();

		for (Set<DMNVertex> set: powerSet){
			int nbFinalOutputs = 0;
			for (DMNVertex vertexOutput: vertexOutputs) {
				if (set.contains(vertexOutput)) {
					nbFinalOutputs++;
				}
				if (nbFinalOutputs == 1){
					int nbStartInputs = 0;
					for (DMNVertex vertexIntput: vertexInputs) {
						if (set.contains(vertexIntput)) {
							nbStartInputs++;
						}
					}
					if (nbStartInputs > 0) {
						newPowerSet.add(set);
					}
				}
			}
		}

		//Generate Subgraphs
		List<Graph<DMNVertex, DMNEdge>> decisionGraphs = new ArrayList<Graph<DMNVertex, DMNEdge>>();

		for (Set<DMNVertex> set: newPowerSet) {
			Graph<DMNVertex, DMNEdge> decisionGraph = new AsSubgraph<DMNVertex, DMNEdge>(dmn, set);

			for (DMNVertex newVertex: decisionGraph.vertexSet()){
				//CONTINUE HERE
				//All output vertices have only one incoming edge?
				//All decision vertices have exactly the same incoming edges as in the original dmn?
				//If both conditions evaluate to true add decisionGraph to decisionGraphs.
			}

		}



		return decisionGraphs;
	}

	public static void saveToDB(String graphName, Graph<DMNVertex, DMNEdge> dmn) {
		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://dmn:OU1yVaZsBXCVjQEE@cluster0.8m4xw.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("dmn");
		database.getCollection(graphName).drop();
		MongoCollection<Document> collection = database.getCollection(graphName);

		for (DMNVertex v : dmn.vertexSet()) {
			DMNVertex vertex = v;
			if (vertex.getVertexType().equals(DMNVertexType.Decision)) {
				Document decision = new Document();

				decision.put("type", vertex.getVertexType().toString());
				decision.put("name", vertex.getVertexName());
				decision.put("description", vertex.getDescription());

				if (vertex.getKnowledgeSource() != null) {
					Document knowledgeSource = new Document();
					knowledgeSource.put("name", vertex.getKnowledgeSource().getName());
					knowledgeSource.put("description",
							vertex.getKnowledgeSource().getDescription());
					knowledgeSource.put("url", vertex.getKnowledgeSource().getUrl().toString());
					decision.put("knowledgeSource", knowledgeSource);
				} else {
					decision.put("knowledgeSource", null);
				}

				if (vertex.getBusinessKnowledge() != null) {
					Document businessKnowledge = new Document();
					businessKnowledge.put("name", vertex.getBusinessKnowledge().getName());
					businessKnowledge.put("description",
							vertex.getBusinessKnowledge().getDescription());
					businessKnowledge.put("url", vertex.getBusinessKnowledge().getUrl().toString());
					decision.put("businessKnowledge", businessKnowledge);
				} else {
					decision.put("businessKnowledge", null);
				}

				if (vertex.getOwner() != null) {
					Document owner = new Document();
					owner.put("name", vertex.getOwner().getName());
					owner.put("description", vertex.getOwner().getDescription());
					owner.put("url", vertex.getOwner().getUrl().toString());
					decision.put("owner", owner);
				} else {
					decision.put("owner", null);
				}

				List<Document> performanceIndicators = new ArrayList<Document>();

				for (PerformanceIndicator perf : vertex.getPerformanceIndicators()) {
					Document performanceIndicator = new Document();
					performanceIndicator.put("name", perf.getName());
					performanceIndicator.put("unit", perf.getUnit());
					performanceIndicator.put("scaleNormalization", perf.getScaleNormalization());
					performanceIndicators.add(performanceIndicator);
				}

				decision.put("performanceIndicators", performanceIndicators);

				List<Document> inputs = new ArrayList<Document>();
				List<Document> outputs = new ArrayList<Document>();

				for (DMNEdge informationRequirement : dmn.incomingEdgesOf(vertex)) {
					DMNVertex inputVertex = dmn.getEdgeSource(informationRequirement);
					Document inputData = new Document();
					inputData.put("type", inputVertex.getVertexType().toString());
					inputData.put("name", inputVertex.getVertexName());
					inputData.put("description", inputVertex.getDescription());
					inputs.add(inputData);
				}

				for (DMNEdge informationItem : dmn.outgoingEdgesOf(vertex)) {
					DMNVertex outputVertex = dmn.getEdgeTarget(informationItem);
					Document outputData = new Document();
					outputData.put("type", outputVertex.getVertexType().toString());
					outputData.put("name", outputVertex.getVertexName());
					outputData.put("description", outputVertex.getDescription());
					outputs.add(outputData);
				}

				decision.put("inputs", inputs);
				decision.put("outputs", outputs);

				InsertOneResult result = collection.insertOne(decision);
				System.out.println(result.getInsertedId().toString());
			}

		}
	}

	public static DMNVertexType vertexTypeFromString(String enumString) {
		DMNVertexType ret = DMNVertexType.Decision;
		switch (enumString) {
			case "InputData":
				ret = DMNVertexType.InputData;
				break;
			case "Decision":
				ret = DMNVertexType.Decision;
				break;
			case "OutputData":
				ret = DMNVertexType.OutputData;
				break;
			default:
				ret = DMNVertexType.Decision;
		}
		return ret;
	}

	public static Graph<DMNVertex, DMNEdge> loadFromDB(String graphName) {
		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://dmn:OU1yVaZsBXCVjQEE@cluster0.8m4xw.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("dmn");
		MongoCollection<Document> collection = database.getCollection(graphName);

		Graph<DMNVertex, DMNEdge> g = new DefaultDirectedGraph<>(DMNEdge.class);

		collection.aggregate(
				Arrays.asList(
						Aggregates.match(Filters.empty())))
				.forEach(doc -> {
					DMNVertex decisionVertex = new DMNVertex(
							vertexTypeFromString(doc.getString("type")),
							doc.getString("name"));
					decisionVertex.setDescription(doc.getString("description"));

					if (doc.get("knowledgeSource") != null) {
						Document knowledgeSourceDoc = doc.get("knowledgeSource",
								Document.class);
						try {
							KnowledgeSource knowledgeSource = new KnowledgeSource(
									knowledgeSourceDoc.getString("name"),
									knowledgeSourceDoc.getString("description"),
									new URL(knowledgeSourceDoc.getString("url")));
							decisionVertex.setKnowledgeSource(knowledgeSource);
						} catch (MalformedURLException e) {
							decisionVertex.setKnowledgeSource(null);
							e.printStackTrace();
						}
					} else {
						decisionVertex.setKnowledgeSource(null);
					}

					if (doc.get("businessKnowledge") != null) {
						Document businessKnowledgeDoc = doc.get("businessKnowledge",
								Document.class);
						try {
							BusinessKnowledgeModel businessKnowledge = new BusinessKnowledgeModel(
									businessKnowledgeDoc.getString("name"),
									businessKnowledgeDoc.getString("description"),
									new URL(businessKnowledgeDoc.getString("url")));
							decisionVertex.setBusinessKnowledge(businessKnowledge);
						} catch (MalformedURLException e) {
							decisionVertex.setBusinessKnowledge(null);
							e.printStackTrace();
						}
					} else {
						decisionVertex.setBusinessKnowledge(null);
					}

					if (doc.get("owner") != null) {
						Document ownerDoc = doc.get("owner", Document.class);
						try {
							OrganisationalUnit owner = new OrganisationalUnit(
									ownerDoc.getString("name"),
									ownerDoc.getString("description"),
									new URL(ownerDoc.getString("url")));
							decisionVertex.setOwner(owner);
						} catch (MalformedURLException e) {
							decisionVertex.setOwner(null);
							e.printStackTrace();
						}
					} else {
						decisionVertex.setOwner(null);
					}

					if (doc.get("performanceIndicators") == null) {
						List<Document> performanceIndicatorsDoc = doc
								.get("performanceIndicators", List.class);
						for (Document perfDoc : performanceIndicatorsDoc) {
							decisionVertex.addPerformanceIndicator(new PerformanceIndicator(
									perfDoc.getString("name"),
									perfDoc.getString("unit"),
									perfDoc.getString("scaleNormalization")));
						}
					}

					g.addVertex(decisionVertex);

					if (doc.get("inputs") != null) {
						List<Document> inputsDoc = doc.get("inputs", List.class);
						for (Document inputDoc : inputsDoc) {
							DMNVertex inputVertex = new DMNVertex(
									vertexTypeFromString(
											inputDoc.getString("type")),
									inputDoc.getString("name"));
							inputVertex.setDescription(inputDoc.getString("description"));
							g.addVertex(inputVertex);
							g.addEdge(inputVertex, decisionVertex, new DMNEdge(
									DMNEdgeType.InformationRequirement));
						}
					}

					if (doc.get("outputs") != null) {
						List<Document> outputsDoc = doc.get("outputs", List.class);
						for (Document outputDoc : outputsDoc) {
							DMNVertex outputVertex = new DMNVertex(
									vertexTypeFromString(
											outputDoc.getString("type")),
									outputDoc.getString("name"));
							outputVertex.setDescription(outputDoc.getString("description"));
							g.addVertex(outputVertex);
							g.addEdge(decisionVertex, outputVertex,
									new DMNEdge(DMNEdgeType.InformationItem));
						}
					}
				});

		return g;
	}

	/*
	 * public Graph<DMNVertex, DMNEdge> loadFromDB(String filePath) {
	 * 
	 * }
	 * 
	 * /* public void addInputData(Graph<DMNVertex, DMNEdge> dmn, ....) {
	 * 
	 * }
	 * 
	 * public void addOutputData(Graph<DMNVertex, DMNEdge> dmn, ....) {
	 * 
	 * }
	 * 
	 * public void addDecision(Graph<DMNVertex, DMNEdge> dmn, List<String>
	 * inputData, List<String> outPutdata, List<PerformanceIndicator>
	 * performanceIndicators, KnowledgeSource knowledgeSource,
	 * BusinessKnowledgeModel knowledgeModel, OrganisationalUnit organisationalUnit)
	 * {
	 * 
	 * }
	 * 
	 * public void semanticSimilaritySearch(List<String> data, List<DMNVertex>
	 * vertices, DMNVertexType dmnVertexType) {
	 * 
	 * }
	 * 
	 * public void matchingAlgorithm(Graph<DMNVertex, DMNEdge> dmn, List<String>
	 * inputData, List<String> outPutdata) {
	 * 
	 * }
	 * 
	 * public void featureClassification() {
	 * 
	 * }
	 * 
	 * public void hierarchicalCriteriaEvaluation() {
	 * 
	 * }
	 * 
	 * public void mcdmAHP() {
	 * 
	 * }
	 */

	/**
	 * Create a toy graph based on String objects.
	 *
	 * @return a graph based on String objects.
	 * @throws MalformedURLException
	 */
	private static void createDMNfromStandard() throws MalformedURLException {
		Graph<DMNVertex, DMNEdge> g = new DefaultDirectedGraph<>(DMNEdge.class);

		DMNVertex decision_requiredMonthlyInstallment = new DMNVertex(DMNVertexType.Decision,
				"Required monthly installment");
		decision_requiredMonthlyInstallment.setDescription(
				"The Required monthly installment decision logic invokes the Installment calculation business knowledge model, passing Requested product.ProductType as the Product Type parameter, Requested product.Rate as the Rate parameter, Requested product.Term as the Term parameter, and Requested product.Amount as the Amount parameter.");
		decision_requiredMonthlyInstallment.setBusinessKnowledge(new BusinessKnowledgeModel(
				"Installment calculation",
				"The Installment calculation decision logic defines a boxed function deriving monthly installment from Product Type, Rate, Term and Amount.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_requiredMonthlyInstallment);

		DMNVertex output_requiredMonthlyInstallment = new DMNVertex(DMNVertexType.OutputData,
				"Required monthly installment");
		output_requiredMonthlyInstallment.setDescription(
				"What is the minimum monthly installment payment required for this loan product? Allowed Answers: A dollar amount greater than zero");
		g.addVertex(output_requiredMonthlyInstallment);

		DMNVertex input_requestedProduct = new DMNVertex(DMNVertexType.InputData, "Requested product");
		input_requestedProduct.setDescription("Details of the loan the applicant has applied for.");
		g.addVertex(input_requestedProduct);

		g.addEdge(input_requestedProduct, decision_requiredMonthlyInstallment,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_requiredMonthlyInstallment, output_requiredMonthlyInstallment,
				new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_applicationRiskScore = new DMNVertex(DMNVertexType.Decision,
				"Application risk score");
		decision_applicationRiskScore.setDescription(
				"The Application Risk Score decision logic invokes the Application risk score model business knowledge model, passing Applicant data. Age as the Age parameter, Applicant data.MaritalStatus as the Marital Status parameter and Applicant data.EmploymentStatus as the Employment Status parameter.");
		decision_applicationRiskScore.setBusinessKnowledge(new BusinessKnowledgeModel(
				"Application risk score model",
				"The Application risk score model decision logic defines a complete, no-order multiple-hit table with aggregation, deriving Application risk score from Age, Marital Status and Employment Status, as the sum of the Partial scores of all matching rows (this is therefore a predictive scorecard represented as a decision table).",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_applicationRiskScore
				.addPerformanceIndicator(new PerformanceIndicator("Monthly bureau costs", "€", ""));
		decision_applicationRiskScore.setOwner(new OrganisationalUnit("Credit risk analytics group",
				"Organization responsible for credit risk models and the use of data to predict credit risk for customers and loan applicants.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_applicationRiskScore);

		DMNVertex output_applicationRiskScore = new DMNVertex(DMNVertexType.OutputData,
				"Application risk score");
		output_applicationRiskScore.setDescription(
				"What is the risk score for this applicant? Allowed Answers: A number greater than 70 and less than 150");
		g.addVertex(output_applicationRiskScore);

		DMNVertex input_applicant_data = new DMNVertex(DMNVertexType.InputData, "Applicant data");
		input_applicant_data.setDescription(
				"Information about the applicant including personal information, marital status and household income/expenses.");
		g.addVertex(input_applicant_data);

		g.addEdge(input_applicant_data, decision_applicationRiskScore,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_applicationRiskScore, output_applicationRiskScore,
				new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_postBureauRiskCategory = new DMNVertex(DMNVertexType.Decision,
				"Post-bureau risk category");
		decision_postBureauRiskCategory.setDescription(
				"The Post-bureau risk category decision logic invokes the Post-bureau risk category business knowledge model, passing Applicant data.ExistingCustomer as the Existing Customer parameter, Bureau data.CreditScore as the Credit Score parameter, and the output of the Application risk score decision as the Application Risk Score parameter. Note that if Bureau data is null (due to the THROUGH strategy bypassing the Collect bureau data task) the Credit Score parameter will be null.");
		decision_postBureauRiskCategory.setBusinessKnowledge(new BusinessKnowledgeModel(
				"Post-bureau risk category table",
				"The Post-bureau risk category table decision logic defines a complete, unique-hit decision table deriving Post-Bureau Risk Category from Existing Customer, Application Risk Score and Credit Score.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_postBureauRiskCategory.setOwner(new OrganisationalUnit("Credit risk",
				"Organization within the bank responsible for defining credit risk strategies and policies and providing tools for managing against these.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_postBureauRiskCategory);

		DMNVertex output_postBureauRiskCategory = new DMNVertex(DMNVertexType.OutputData,
				"Post-bureau risk category");
		output_postBureauRiskCategory.setDescription(
				"Which risk category is most appropriate for this applicant given all available data? Allowed Answers: A value from the explicit list 'Decline', 'High Risk', 'Medium Risk', 'Low Risk', 'Very Low Risk'");
		g.addVertex(output_postBureauRiskCategory);

		DMNVertex input_bureau_data = new DMNVertex(DMNVertexType.InputData, "Bureau data");
		input_bureau_data.setDescription(
				"External credit score and bankruptcy information provided by a bureau.");
		g.addVertex(input_bureau_data);

		g.addEdge(input_bureau_data, decision_postBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(input_applicant_data, decision_postBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_applicationRiskScore, decision_postBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_postBureauRiskCategory, output_postBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_preBureauRiskCategory = new DMNVertex(DMNVertexType.Decision,
				"Pre-bureau risk category");
		decision_preBureauRiskCategory.setDescription(
				"The Pre-Bureau Risk Category decision logic invokes the Pre-bureau risk category table business knowledge model, passing Applicant data.ExistingCustomer as the Existing Customer parameter and the output of the Application risk score decision as the Application Risk Score parameter.");
		decision_preBureauRiskCategory.setBusinessKnowledge(new BusinessKnowledgeModel(
				"Pre-bureau risk category table",
				"The Pre-bureau risk category table decision logic defines a complete, unique-hit decision table deriving Pre- bureau risk category from Existing Customer and Application Risk Score.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_preBureauRiskCategory.setOwner(new OrganisationalUnit("Credit risk",
				"Organization within the bank responsible for defining credit risk strategies and policies and providing tools for managing against these.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_preBureauRiskCategory);

		DMNVertex output_preBureauRiskCategory = new DMNVertex(DMNVertexType.OutputData,
				"Pre-bureau risk category");
		output_preBureauRiskCategory.setDescription(
				"Which risk category is most appropriate for this applicant given only their application data? Allowed Answers: Value from explicit list 'Decline', 'High Risk', 'Medium Risk', 'Low Risk', 'Very Low Risk'");
		g.addVertex(output_preBureauRiskCategory);

		g.addEdge(input_applicant_data, decision_preBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_applicationRiskScore, decision_preBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_preBureauRiskCategory, output_preBureauRiskCategory,
				new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_postBureauAffordability = new DMNVertex(DMNVertexType.Decision,
				"Post-bureau affordability");
		decision_postBureauAffordability.setDescription(
				"The Post-bureau affordability decision logic invokes the Affordability calculation business knowledge model, passing Applicant data.Monthly.Income as the Monthly Income parameter, Applicant data.Monthly.Repayments as the Monthly Repayments parameter, Applicant data.Monthly.Expenses as the Monthly Expenses parameter, the output of the Post-bureau risk category decision as the Risk Category parameter, and the output of the Required monthly installment decision as the Required Monthly Installment parameter.");
		decision_postBureauAffordability.setBusinessKnowledge(new BusinessKnowledgeModel(
				"Affordability calculation",
				"The Affordability calculation decision logic defines a boxed function deriving Affordability from Monthly Income, Monthly Repayments, Monthly Expenses and Required Monthly Installment. One step in this calculation derives Credit contingency factor by invoking the Credit contingency factor table business.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_postBureauAffordability);

		DMNVertex output_postBureauAffordability = new DMNVertex(DMNVertexType.OutputData,
				"Post-bureau affordability");
		output_postBureauAffordability.setDescription(
				"Can the applicant afford the loan they applied for given all available data? Allowed Answers: Yes/No");
		g.addVertex(output_postBureauAffordability);

		g.addEdge(output_postBureauRiskCategory, decision_postBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_requiredMonthlyInstallment, decision_postBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(input_applicant_data, decision_postBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_postBureauAffordability, output_postBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_preBureauAffordability = new DMNVertex(DMNVertexType.Decision,
				"Pre-bureau affordability");
		decision_preBureauAffordability.setDescription(
				"The Pre-bureau affordability decision logic invokes the Affordability calculation business knowledge model, passing Applicant data.Monthly.Income as the Monthly Income parameter, Applicant data.Monthly.Repayments as the Monthly Repayments parameter, Applicant data.Monthly.Expenses as the Monthly Expenses parameter, the output of the Pre-bureau risk category decision as the Risk Category parameter, and the output of the Required monthly installment decision as the Required Monthly Installment parameter.");
		decision_preBureauAffordability.setBusinessKnowledge(new BusinessKnowledgeModel(
				"Affordability calculation",
				"The Affordability calculation decision logic defines a boxed function deriving Affordability from Monthly Income, Monthly Repayments, Monthly Expenses and Required Monthly Installment. One step in this calculation derives Credit contingency factor by invoking the Credit contingency factor table business.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_preBureauAffordability);

		DMNVertex output_preBureauAffordability = new DMNVertex(DMNVertexType.OutputData,
				"Pre-bureau affordability");
		output_preBureauAffordability.setDescription(
				"Can the applicant afford the loan they applied for given only their application data? Allowed Answers: Yes/No");
		g.addVertex(output_preBureauAffordability);

		g.addEdge(output_requiredMonthlyInstallment, decision_preBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(input_applicant_data, decision_preBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_preBureauRiskCategory, decision_preBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_preBureauAffordability, output_preBureauAffordability,
				new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_routing = new DMNVertex(DMNVertexType.Decision, "Routing");
		decision_routing.setDescription(
				"The Routing decision logic invokes the Routing rules business knowledge model, passing Bureau data. Bankrupt as the Bankrupt parameter, Bureau data. Credit Score as the Credit Score parameter, the output of the Post- bureau risk category decision as the Post-Bureau Risk Category parameter, and the output of the Post-bureau affordability decision as the Post-Bureau Affordability parameter. Note that if Bureau data is null (due to the THROUGH strategy bypassing the Collect bureau data task) the Bankrupt and Credit Score parameters will be null.");
		decision_routing.setBusinessKnowledge(new BusinessKnowledgeModel("Routing rules",
				"The Routing Rules decision logic defines a complete, priority-ordered single hit decision table deriving Routing from Post-Bureau Risk Category, Post-Bureau Affordability, Bankrupt and Credit Score.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_routing.setOwner(new OrganisationalUnit("Credit risk",
				"Organization within the bank responsible for defining credit risk strategies and policies and providing tools for managing against these.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_routing.addPerformanceIndicator(new PerformanceIndicator("Monthly loan accept rate", "%", ""));
		decision_routing.addPerformanceIndicator(
				new PerformanceIndicator("Monthly value of loans written", "", ""));
		decision_routing.addPerformanceIndicator(
				new PerformanceIndicator("Monthly auto-adjudication rate", "%", ""));
		decision_routing.addPerformanceIndicator(
				new PerformanceIndicator("Auto adjudication rate 90%", "months", ""));
		g.addVertex(decision_routing);

		DMNVertex output_routing = new DMNVertex(DMNVertexType.OutputData, "Routing");
		output_routing.setDescription(
				"How this should this applicant be routed given all available data? Allowed Answers: A value from the explicit list 'Decline', 'Refer for Adjudication', 'Accept without Review'");
		g.addVertex(output_routing);

		g.addEdge(input_bureau_data, decision_routing, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_postBureauRiskCategory, decision_routing,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_postBureauAffordability, decision_routing,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_routing, output_routing, new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_Eligibility = new DMNVertex(DMNVertexType.Decision, "eligibility");
		decision_Eligibility.setDescription(
				"The Eligibility decision logic invokes the Eligibility rules business knowledge model, passing Applicant data.Age as the Age parameter, the output of the Pre-bureau risk category decision as the Pre-Bureau Risk Category parameter, and the output of the Pre-bureau affordability decision as the Pre-Bureau Affordability parameter.");
		decision_Eligibility.setBusinessKnowledge(new BusinessKnowledgeModel("eligibility rules",
				"The Eligibility rules decision logic defines a complete, priority-ordered single hit decision table deriving Eligibility from Pre-Bureau Risk Category, Pre-Bureau Affordability and Age.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_Eligibility.setOwner(new OrganisationalUnit("Credit risk",
				"Organization within the bank responsible for defining credit risk strategies and policies and providing tools for managing against these.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_Eligibility);

		DMNVertex output_Eligibility = new DMNVertex(DMNVertexType.OutputData, "eligibility");
		output_Eligibility.setDescription(
				"Does this applicant appear eligible for the loan they applied for given only their application data? Allowed Answers: Value from the explicit list 'Eligible', 'Not Eligible'");
		g.addVertex(output_Eligibility);

		g.addEdge(output_preBureauAffordability, decision_Eligibility,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(input_applicant_data, decision_Eligibility, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_preBureauRiskCategory, decision_Eligibility,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_Eligibility, output_Eligibility, new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_bureauCallType = new DMNVertex(DMNVertexType.Decision, "Bureau call type");
		decision_bureauCallType.setDescription(
				"The Bureau call type decision logic invokes the Bureau call type table, passing the output of the Pre-bureau risk category decision as the Pre-Bureau Risk Category parameter.");
		decision_bureauCallType.setBusinessKnowledge(new BusinessKnowledgeModel("Bureau call type table",
				"The Bureau call type table decision logic defines a complete, unique-hit decision table deriving Bureau Call Type from Pre-Bureau Risk Category.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_bureauCallType
				.addPerformanceIndicator(new PerformanceIndicator("Monthly bureau costs", "€", ""));
		decision_bureauCallType.setOwner(new OrganisationalUnit("Credit risk",
				"Organization within the bank responsible for defining credit risk strategies and policies and providing tools for managing against these.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_bureauCallType);

		DMNVertex output_bureauCallType = new DMNVertex(DMNVertexType.OutputData, "Bureau call type");
		output_bureauCallType.setDescription(
				"How much data should be requested from the credit bureau for this application? Allowed Answers: A value from the explicit list 'Full', 'Mini', 'None'");
		g.addVertex(output_bureauCallType);

		g.addEdge(output_preBureauRiskCategory, decision_bureauCallType,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_bureauCallType, output_bureauCallType, new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_adjudication = new DMNVertex(DMNVertexType.Decision, "Adjudication");
		decision_adjudication.setDescription(
				"Determine if an application requiring adjudication should be accepted or declined given the available application data and supporting documents.");
		decision_adjudication.setKnowledgeSource(new KnowledgeSource("Credit officer experience",
				"The collected wisdom of the credit officers as collected in their best practice wiki.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		decision_adjudication
				.addPerformanceIndicator(new PerformanceIndicator("Monthly loan accept rate", "%", ""));
		decision_adjudication
				.addPerformanceIndicator(
						new PerformanceIndicator("Monthly value of loans written", "", ""));
		decision_adjudication.setOwner(new OrganisationalUnit("Credit risk",
				"Organization within the bank responsible for defining credit risk strategies and policies and providing tools for managing against these.",
				new URL("https://www.omg.org/spec/DMN/1.3/PDF")));
		g.addVertex(decision_adjudication);

		DMNVertex output_adjudication = new DMNVertex(DMNVertexType.OutputData, "Adjudication");
		output_adjudication.setDescription(
				"Should this application that has been referred for adjudication be accepted? Allowed Answers: Yes/No");
		g.addVertex(output_adjudication);

		DMNVertex input_supporting_documents = new DMNVertex(DMNVertexType.InputData, "Supporting documents");
		input_supporting_documents.setDescription(
				"Documents associated with a loan that are not processed electronically but are available for manual adjudication.");
		g.addVertex(input_supporting_documents);

		g.addEdge(input_supporting_documents, decision_adjudication,
				new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(input_bureau_data, decision_adjudication, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_routing, decision_adjudication, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(input_applicant_data, decision_adjudication, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_adjudication, output_adjudication, new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		DMNVertex decision_strategy = new DMNVertex(DMNVertexType.Decision, "Strategy");
		decision_strategy.setDescription(
				"The Strategy decision logic defines a complete, unique-hit decision table deriving Strategy from Eligibility and Bureau call type.");
		decision_strategy
				.addPerformanceIndicator(new PerformanceIndicator("Monthly loan accept rate", "%", ""));
		decision_strategy.addPerformanceIndicator(
				new PerformanceIndicator("Monthly value of loans written", "", ""));
		decision_strategy.addPerformanceIndicator(
				new PerformanceIndicator("Monthly auto-adjudication rate", "%", ""));
		decision_strategy.addPerformanceIndicator(
				new PerformanceIndicator("Auto adjudication rate 90%", "months", ""));
		g.addVertex(decision_strategy);

		DMNVertex output_strategy = new DMNVertex(DMNVertexType.OutputData, "Strategy");
		output_strategy.setDescription(
				"What is the appropriate handling strategy for this application? Allowed Answers: A value from the explicit list 'Decline','Bureau','Through'");
		g.addVertex(output_strategy);

		g.addEdge(output_Eligibility, decision_strategy, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(output_bureauCallType, decision_strategy, new DMNEdge(DMNEdgeType.InformationRequirement));
		g.addEdge(decision_strategy, output_strategy, new DMNEdge(DMNEdgeType.InformationItem));

		// ----------------------------------------------------------------

		saveToDB("alphaBank", g);
	}
}
