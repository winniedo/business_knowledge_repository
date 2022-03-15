package de.dmn.service.graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.dmn.model.Decision;
import de.dmn.model.InputData;
import de.dmn.model.OutputData;
import de.dmn.model.PerformanceIndicator;
import de.dmn.model.ReactEdge;
import de.dmn.model.ReactGraph;
import de.dmn.model.ReactNode;
import de.dmn.model.ReactResults;
import de.dmn.model.TestResults;
import de.dmn.repository.DecisionRepository;
import de.dmn.repository.InputDataRepository;
import de.dmn.repository.OutputDataRepository;
import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.TextSimilarity;
import de.linguatools.disco.DISCO;

@Component
public class GraphManagement {

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private InputDataRepository inputDataRepository;

    @Autowired
    private OutputDataRepository outputDataRepository;

    public Graph<DMNVertex, DMNEdge> loadFromDB() {
        Graph<DMNVertex, DMNEdge> g = new DefaultDirectedGraph<>(DMNEdge.class);

        List<Decision> decisions = decisionRepository.findAll();

        for (Decision decision : decisions) {
            g.addVertex(decision);
            for (InputData inputData : decision.getInputData()) {
                g.addVertex(inputData);
                g.addEdge(inputData, decision, new InformationRequirement());
            }
            for (OutputData outputAsInputData : decision.getOutputAsInputData()) {
                g.addVertex(outputAsInputData);
                g.addEdge(outputAsInputData, decision, new InformationRequirement());
            }
            for (OutputData outputData : decision.getOutputData()) {
                g.addVertex(outputData);
                g.addEdge(decision, outputData, new InformationItem());
            }
        }

        return g;
    }

    public <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }       
        return sets;
    }

    

    public TestResults bruteForcePerformanceTestPython(Integer depth, Integer decToOutputEdges, Integer inputToDecEdges, Integer outputToDecEdges) {

        Integer numberOfNodes=0;
        Integer numberOfEdges=0;
        Integer numberOfInputNodes=0;
        Integer numberOfOutputNodes=0;
        Integer numberOfDecisionNodes=0;
        Long booleanExprTime=0L;
        Long DNF_time=0L;
        Integer numberDNF=0;
        Long graphBuildingTime=0L;

        Long startTime;
        Long endTime;

        Graph<String, SimpleEdge> g = new DefaultDirectedGraph<>(SimpleEdge.class);

        List<List<String>> nodeList = new ArrayList<List<String>>();

        Integer outIdx = 0;
        Integer inpIdx = 0;
        Integer decIdx = 0;

        Set<String> nodeSet = new HashSet<String>();

        // Generate top output node
        String topNode ="o[" + outIdx.toString() + "]";
        g.addVertex(topNode);
        nodeSet.add(topNode);
        outIdx++;

        List<String> list0 = new ArrayList<String>();
        list0.add(topNode);

        nodeList.add(list0);

        for (int i = 1; i <= depth; i++) {
            List<String> newList = new ArrayList<String>();

            for (String vertex : nodeList.get(i - 1)) {
                if (vertex.startsWith("o")) {
                    for (int j = 1; j <= decToOutputEdges; j++) {
                        String node = "d[" + decIdx.toString() + "]";
                        nodeSet.add(node);
                        decIdx++;
                        g.addVertex(node);
                        g.addEdge(node, vertex);
                        numberOfEdges++;
                        newList.add(node);
                    }
                }
                if (vertex.startsWith("d")) {
                    for (int j = 1; j <= inputToDecEdges; j++) {
                        String node = "i[" + inpIdx.toString() + "]";
                        nodeSet.add(node);
                        inpIdx++;
                        g.addVertex(node);
                        g.addEdge(node, vertex);
                        numberOfEdges++;
                    }
                    if (i < depth - 1) {
                        for (int j = 1; j <= outputToDecEdges; j++) {
                            String node = "o[" + outIdx.toString() + "]";
                            nodeSet.add(node);
                            outIdx++;
                            g.addVertex(node);
                            g.addEdge(node, vertex);
                            numberOfEdges++;
                            newList.add(node);
                        }
                    }
                }
            }

            nodeList.add(newList);
        }

        numberOfNodes = inpIdx + outIdx + decIdx;
        numberOfInputNodes = inpIdx;
        numberOfOutputNodes = outIdx;
        numberOfDecisionNodes = decIdx;
        /*----*/
        String nodeSetStr = "";
        for (String nodeInter: nodeSet) {
            if (nodeSetStr.equals("")){
                nodeSetStr = nodeSetStr + nodeInter;
            } else {
                nodeSetStr = nodeSetStr + ", " + nodeInter;
            }
        }

        System.out.println("########################" + nodeSetStr);

        startTime = System.currentTimeMillis();
        Set<String> powerSet = new HashSet<String>();
        try {
            startTime = System.currentTimeMillis();
            String[] command = new String[] {
                    "python3",
                    "/home/kuosi/dev/master_thesis/06_Implementation/02_boolean_algebra/power_set.py",
                    nodeSetStr
            };
            String inputLine = "Y";

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            writer.write(inputLine);
            writer.newLine();
            writer.close();

            String line;
            numberDNF=0;

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                powerSet.add(line);
                numberDNF++;
            }
            endTime = System.currentTimeMillis();
            DNF_time = endTime - startTime;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();     
        booleanExprTime = endTime - startTime;

        System.out.println(powerSet.size());

        startTime = System.currentTimeMillis();
        for (String foundSet: powerSet) {
            //only set of size >= 3 are correct
            String[] ll = foundSet.split(",");
            if (ll.length>=3){
                //Check if the set contains at least the top node
                //Check if the set contains 
            }
        }
        endTime = System.currentTimeMillis();
        DNF_time = endTime - startTime;
        /*----*/
        return new TestResults(numberOfNodes, numberOfEdges, numberOfInputNodes, numberOfOutputNodes, numberOfDecisionNodes, booleanExprTime, DNF_time, numberDNF, graphBuildingTime);
    }

    

    public TestResults bruteForcePerformanceTest(Integer depth, Integer decToOutputEdges, Integer inputToDecEdges, Integer outputToDecEdges) {

        Integer numberOfNodes=0;
        Integer numberOfEdges=0;
        Integer numberOfInputNodes=0;
        Integer numberOfOutputNodes=0;
        Integer numberOfDecisionNodes=0;
        Long booleanExprTime=0L;
        Long DNF_time=0L;
        Integer numberDNF=0;
        Long graphBuildingTime=0L;

        Long startTime;
        Long endTime;

        Graph<String, SimpleEdge> g = new DefaultDirectedGraph<>(SimpleEdge.class);

        List<List<String>> nodeList = new ArrayList<List<String>>();

        Integer outIdx = 0;
        Integer inpIdx = 0;
        Integer decIdx = 0;

        Set<String> nodeSet = new HashSet<String>();

        // Generate top output node
        String topNode ="o[" + outIdx.toString() + "]";
        g.addVertex(topNode);
        nodeSet.add(topNode);
        outIdx++;

        List<String> list0 = new ArrayList<String>();
        list0.add(topNode);

        nodeList.add(list0);

        for (int i = 1; i <= depth; i++) {
            List<String> newList = new ArrayList<String>();

            for (String vertex : nodeList.get(i - 1)) {
                if (vertex.startsWith("o")) {
                    for (int j = 1; j <= decToOutputEdges; j++) {
                        String node = "d[" + decIdx.toString() + "]";
                        nodeSet.add(node);
                        decIdx++;
                        g.addVertex(node);
                        g.addEdge(node, vertex);
                        numberOfEdges++;
                        newList.add(node);
                    }
                }
                if (vertex.startsWith("d")) {
                    for (int j = 1; j <= inputToDecEdges; j++) {
                        String node = "i[" + inpIdx.toString() + "]";
                        nodeSet.add(node);
                        inpIdx++;
                        g.addVertex(node);
                        g.addEdge(node, vertex);
                        numberOfEdges++;
                    }
                    if (i < depth - 1) {
                        for (int j = 1; j <= outputToDecEdges; j++) {
                            String node = "o[" + outIdx.toString() + "]";
                            nodeSet.add(node);
                            outIdx++;
                            g.addVertex(node);
                            g.addEdge(node, vertex);
                            numberOfEdges++;
                            newList.add(node);
                        }
                    }
                }
            }

            nodeList.add(newList);
        }

        numberOfNodes = inpIdx + outIdx + decIdx;
        numberOfInputNodes = inpIdx;
        numberOfOutputNodes = outIdx;
        numberOfDecisionNodes = decIdx;

        startTime = System.currentTimeMillis();        
        Set<Set<String>> powerSet = Sets.powerSet(nodeSet);
        endTime = System.currentTimeMillis();     
        booleanExprTime = endTime - startTime;
        
        System.out.println(powerSet);

        startTime = System.currentTimeMillis();
        for (Set<String> foundSet: powerSet) {
            //only set of size >= 3 are correct
            if (foundSet.size()>=3){
                //Check if the set contains at one and only one top output node
                int found = 0;
                for (String node: foundSet){
                    if (node.startsWith("o")){
                        if (g.outgoingEdgesOf(node).size()==0){
                            found++;
                        }
                        found++;
                    }
                    if (node.startsWith("d")){
                        found++;
                    }
                    if (node.startsWith("i")){
                        found++;
                    }
                }
            }
        }
        endTime = System.currentTimeMillis();
        DNF_time = endTime - startTime;

        return new TestResults(numberOfNodes, numberOfEdges, numberOfInputNodes, numberOfOutputNodes, numberOfDecisionNodes, booleanExprTime, DNF_time, numberDNF, graphBuildingTime);
    }

    

    public TestResults performanceTest(Integer depth, Integer decToOutputEdges, Integer inputToDecEdges, Integer outputToDecEdges) {

        Integer numberOfNodes=0;
        Integer numberOfEdges=0;
        Integer numberOfInputNodes=0;
        Integer numberOfOutputNodes=0;
        Integer numberOfDecisionNodes=0;
        Long booleanExprTime=0L;
        Long DNF_time=0L;
        Integer numberDNF=0;
        Long graphBuildingTime=0L;

        Long startTime;
        Long endTime;

        Graph<String, SimpleEdge> g = new DefaultDirectedGraph<>(SimpleEdge.class);

        List<List<String>> nodeList = new ArrayList<List<String>>();

        Integer outIdx = 0;
        Integer inpIdx = 0;
        Integer decIdx = 0;

        // Generate top output node
        String topNode ="o[" + outIdx.toString() + "]";
        g.addVertex(topNode);
        outIdx++;

        List<String> list0 = new ArrayList<String>();
        list0.add(topNode);

        nodeList.add(list0);

        for (int i = 1; i <= depth; i++) {
            List<String> newList = new ArrayList<String>();

            for (String vertex : nodeList.get(i - 1)) {
                if (vertex.startsWith("o")) {
                    for (int j = 1; j <= decToOutputEdges; j++) {
                        String node = "d[" + decIdx.toString() + "]";
                        decIdx++;
                        g.addVertex(node);
                        g.addEdge(node, vertex);
                        numberOfEdges++;
                        newList.add(node);
                    }
                }
                if (vertex.startsWith("d")) {
                    for (int j = 1; j <= inputToDecEdges; j++) {
                        String node = "i[" + inpIdx.toString() + "]";
                        inpIdx++;
                        g.addVertex(node);
                        g.addEdge(node, vertex);
                        numberOfEdges++;
                    }
                    if (i < depth - 1) {
                        for (int j = 1; j <= outputToDecEdges; j++) {
                            String node = "o[" + outIdx.toString() + "]";
                            outIdx++;
                            g.addVertex(node);
                            g.addEdge(node, vertex);
                            numberOfEdges++;
                            newList.add(node);
                        }
                    }
                }
            }

            nodeList.add(newList);
        }

        numberOfNodes = inpIdx + outIdx + decIdx;
        numberOfInputNodes = inpIdx;
        numberOfOutputNodes = outIdx;
        numberOfDecisionNodes = decIdx;

        startTime = System.currentTimeMillis();
        System.out.println(startTime);
        String expr = booleanExpr(g, topNode);
        endTime = System.currentTimeMillis();        
        System.out.println(endTime);
        booleanExprTime = endTime - startTime;
        System.out.println(expr);

        List<String> dnfResult = new ArrayList<String>();

        try {
            startTime = System.currentTimeMillis();
            String[] command = new String[] {
                    "python3",
                    "/home/kuosi/dev/master_thesis/06_Implementation/02_boolean_algebra/dnf.py",
                    expr
            };
            String inputLine = "Y";

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            writer.write(inputLine);
            writer.newLine();
            writer.close();

            String line;
            numberDNF=0;

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                dnfResult.add(line);
                numberDNF++;
            }
            endTime = System.currentTimeMillis();
            DNF_time = endTime - startTime;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        for (String dnf: dnfResult){
            Graph<String, SimpleEdge> newGraph = new DefaultDirectedGraph<>(SimpleEdge.class);
            List<String> newOutputList = new ArrayList<String>();
            List<String> newDecisionList = new ArrayList<String>();            
            List<String> newInputList = new ArrayList<String>();
            for (String strNode: dnf.split(" ")){
                //If node in user input data
                if (strNode.startsWith("o")) {
                    newGraph.addVertex(strNode);
                    newOutputList.add(strNode);
                }
                if (strNode.startsWith("i")) {
                    newGraph.addVertex(strNode);
                    newInputList.add(strNode);
                }
                if (strNode.startsWith("d")) {
                    newGraph.addVertex(strNode);
                    newDecisionList.add(strNode);
                }
            }
            
            for (String vertex: newOutputList){
                for (SimpleEdge edge: g.incomingEdgesOf(vertex)){ 
                    String sourceVertex = edge.getSource();
                    if (newDecisionList.contains(sourceVertex)){
                        newGraph.addEdge(sourceVertex, vertex);
                    }
                }
            }
            for (String vertex: newDecisionList){
                for (SimpleEdge edge: g.incomingEdgesOf(vertex)){ 
                    String sourceVertex = edge.getSource();
                    if (newOutputList.contains(sourceVertex) || newInputList.contains(sourceVertex)){
                        newGraph.addEdge(sourceVertex, vertex);
                    }
                }
            }
        }
        endTime = System.currentTimeMillis();
        graphBuildingTime = endTime - startTime;

        return new TestResults(numberOfNodes, numberOfEdges, numberOfInputNodes, numberOfOutputNodes, numberOfDecisionNodes, booleanExprTime, DNF_time, numberDNF, graphBuildingTime);
    }

    /**
     * Calculate the boolean expression of a vertex in an eDRD
     * @param g: eDRD
     * @param vertex
     * @return Boolean expression
     */
    public String booleanExpr(Graph<String, SimpleEdge> g, String vertex) {
        if (vertex.startsWith("i")) {
            return vertex;
        }

        if (vertex.startsWith("o")) {
            String expr = "(" + vertex + "&(";
            int i = 0;
            for (SimpleEdge edge : g.incomingEdgesOf(vertex)) {
                if (i == 0) {
                    expr = expr + booleanExpr(g, edge.getSource());
                    i++;
                } else {
                    expr = expr + "|" + booleanExpr(g, edge.getSource());
                }
            }

            expr = expr + "))";
            return expr;
        }

        if (vertex.startsWith("d")) {
            String expr = "(" + vertex + "&(";
            int i = 0;
            for (SimpleEdge edge : g.incomingEdgesOf(vertex)) {
                if (i == 0) {
                    expr = expr + booleanExpr(g, edge.getSource());
                    i++;
                } else {
                    expr = expr + "&" + booleanExpr(g, edge.getSource());
                }
            }

            expr = expr + "))";
            return expr;
        }
        return "";
    }

    public ReactGraph convertFromJavaToReact(Graph<DMNVertex, DMNEdge> g) {
        List<ReactNode> nodes = new ArrayList<ReactNode>();
        List<ReactEdge> edges = new ArrayList<ReactEdge>();

        for (DMNVertex dmnVertex : g.vertexSet()) {
            if (dmnVertex.getClass() == InputData.class) {
                InputData node = (InputData) dmnVertex;
                nodes.add(new ReactNode("input", "" + node.getId(), node.getName()));
            }
            if (dmnVertex.getClass() == OutputData.class) {
                OutputData node = (OutputData) dmnVertex;
                nodes.add(new ReactNode("output", "" + node.getId(), node.getName()));
            }
            if (dmnVertex.getClass() == Decision.class) {
                Decision node = (Decision) dmnVertex;
                nodes.add(new ReactNode("decision", "" + node.getId(), node.getName()));

                for (DMNEdge dmnEdge : g.outgoingEdgesOf(dmnVertex)) {
                    OutputData target = (OutputData) g.getEdgeTarget(dmnEdge);
                    edges.add(new ReactEdge("informationItem",
                            new ReactNode("decision", "" + node.getId(), node.getName()),
                            new ReactNode("output", "" + target.getId(), target.getName())));
                }

                for (DMNEdge dmnEdge : g.incomingEdgesOf(dmnVertex)) {
                    DMNVertex sourceVertex = g.getEdgeSource(dmnEdge);
                    if (sourceVertex.getClass() == OutputData.class) {
                        OutputData source = (OutputData) sourceVertex;
                        edges.add(new ReactEdge("informationRequirement",
                                new ReactNode("output", "" + source.getId(), source.getName()),
                                new ReactNode("decision", "" + node.getId(), node.getName())));
                    }
                    if (sourceVertex.getClass() == InputData.class) {
                        InputData source = (InputData) sourceVertex;
                        edges.add(new ReactEdge("informationRequirement",
                                new ReactNode("input", "" + source.getId(), source.getName()),
                                new ReactNode("decision", "" + node.getId(), node.getName())));
                    }
                }
            }
        }

        ReactGraph reactGraph = new ReactGraph(nodes, edges);
        return reactGraph;
    }

    public Graph<DMNVertex, DMNEdge> convertFromReactToJava(ReactGraph reactGraph) {
        Graph<DMNVertex, DMNEdge> g = new DefaultDirectedGraph<>(DMNEdge.class);

        for (ReactNode reactNode : reactGraph.getNodes()) {
            if (reactNode.getType().equals("input")) {
                InputData foundInputData = inputDataRepository.findByName(reactNode.getName()).get(0);
                g.addVertex(foundInputData);
            }
            if (reactNode.getType().equals("output")) {
                OutputData foundOutputData = outputDataRepository.findByName(reactNode.getName()).get(0);
                g.addVertex(foundOutputData);
            }
            if (reactNode.getType().equals("decision")) {
                Decision foundDecision = decisionRepository.findByName(reactNode.getName()).get(0);
                g.addVertex(foundDecision);
            }
        }

        for (ReactEdge reactEdge : reactGraph.getEdges()) {
            ReactNode source = reactEdge.getSource();
            ReactNode target = reactEdge.getDestination();

            if (source.getType().equals("decision")) {

                Decision foundDecision = decisionRepository.findByName(source.getName()).get(0);
                OutputData foundOutputData = outputDataRepository.findByName(target.getName()).get(0);
                g.addEdge(foundDecision, foundOutputData, new InformationItem());

            } else {

                Decision foundDecision = decisionRepository.findByName(target.getName()).get(0);

                if (source.getType().equals("input")) {
                    InputData foundInputData = inputDataRepository.findByName(source.getName()).get(0);
                    g.addEdge(foundInputData, foundDecision, new InformationRequirement());
                } else {
                    OutputData foundOutputData = outputDataRepository.findByName(source.getName()).get(0);
                    g.addEdge(foundOutputData, foundDecision, new InformationRequirement());
                }

            }
        }

        return g;
    }

    public ReactGraph getInitialGraph() {
        return convertFromJavaToReact(loadFromDB());
    }

    /**
     * Use the DISCO library to compute the semantic similarity measure between text1 and text2
     * @param disco: loaded disco database
     * @param text1
     * @param text2
     * @return semantic similarity measure between text1 and text2
     * @throws IOException
     * @throws InterruptedException
     * @throws CorruptConfigFileException
     */
    public float checkSimilarity(DISCO disco, String text1, String text2)
            throws IOException, InterruptedException, CorruptConfigFileException {
        float ts = TextSimilarity.textSimilarity(text1.strip().toLowerCase(), text2.strip().toLowerCase(), disco,
                DISCO.SimilarityMeasure.COSINE);
        System.out.println(text1 + "  vs  " + text2 + "  =  " + ts);
        return ts;
    }

    /**
     * Check if a decision fulfills all elements from a list of business objectives.
     * @param businessObjectives: list of business objectives
     * @param dec: decision
     * @param threshold: semantic similarity threshold
     * @param disco: loaded disco database
     * @return True if dec fulfills all elements from businessObjectives, else False
     * @throws IOException
     * @throws InterruptedException
     * @throws CorruptConfigFileException
     */
    public boolean businessObjectiveEvaluation(List<String> businessObjectives, Decision dec, Double threshold, DISCO disco) throws IOException, InterruptedException, CorruptConfigFileException {
        boolean objectiveMatch = true;
        for (String ob: businessObjectives) {
            if (ob.split("<=").length == 2){
                String performanceIndicator = ob.split("<=")[0].strip();
                Double value = Double.parseDouble(ob.split("<=")[1].strip());
                for (PerformanceIndicator perf: dec.getPerformanceIndicators()) {
                    if (checkSimilarity(disco, performanceIndicator, perf.getName()) >= threshold){
                        if (!(perf.getCombinedRating() <= value)) {
                            objectiveMatch = false;
                        }
                    }
                }
            } else if (ob.split(">=").length == 2){
                String performanceIndicator = ob.split(">=")[0].strip();
                Double value = Double.parseDouble(ob.split(">=")[1].strip());
                for (PerformanceIndicator perf: dec.getPerformanceIndicators()) {
                    if (checkSimilarity(disco, performanceIndicator, perf.getName()) >= threshold){
                        if (!(perf.getCombinedRating() >= value)) {
                            objectiveMatch = false;
                        }
                    }
                }
            } else if (ob.split(">").length == 2){
                String performanceIndicator = ob.split(">")[0].strip();
                Double value = Double.parseDouble(ob.split(">")[1].strip());
                for (PerformanceIndicator perf: dec.getPerformanceIndicators()) {
                    if (checkSimilarity(disco, performanceIndicator, perf.getName()) >= threshold){
                        if (!(perf.getCombinedRating() > value)) {
                            objectiveMatch = false;
                        }
                    }
                }
            } else if (ob.split("<").length == 2){
                String performanceIndicator = ob.split("<")[0].strip();
                Double value = Double.parseDouble(ob.split("<")[1].strip());
                for (PerformanceIndicator perf: dec.getPerformanceIndicators()) {
                    if (checkSimilarity(disco, performanceIndicator, perf.getName()) >= threshold){
                        if (!(perf.getCombinedRating() < value)) {
                            objectiveMatch = false;
                        }
                    }
                }
            } else if (ob.split("==").length == 2){
                String performanceIndicator = ob.split("==")[0].strip();
                Double value = Double.parseDouble(ob.split("==")[1].strip());
                for (PerformanceIndicator perf: dec.getPerformanceIndicators()) {
                    if (checkSimilarity(disco, performanceIndicator, perf.getName()) >= threshold){
                        if (!(perf.getCombinedRating()!= value)) {
                            objectiveMatch = false;
                        }
                    }
                }
            }
        }
        return objectiveMatch;
    }

    /**
     * Search all eDRD (reusable business knowledge) that match user provided business objectives, input data and output data
     * @param businessObjectives: user provided business objectives
     * @param userInputData: user provided input data
     * @param userOutputData: user provided output data
     * @param threshold: threshold to be used for calculating the semantic similarity measure
     * @return the list of eDRD (reusable business knowledge) that match user provided business objectives, input data and output data
     * @throws CorruptIndexException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws CorruptConfigFileException
     * @throws InterruptedException
     */
    public ReactResults matchingAlgorithm(List<String> businessObjectives, List<String> userInputData, List<String> userOutputData, Double threshold) throws CorruptIndexException, FileNotFoundException, IOException, CorruptConfigFileException, InterruptedException{

        String semanticSimilarOutputData="";
        String semanticSimilarInputData="";
        List<String> MCS = new ArrayList<String>();
        List<String> MCSMatchingInputsObjective = new ArrayList<String>();
        List<ReactGraph> reactGraphs = new ArrayList<ReactGraph>();

        Graph<DMNVertex, DMNEdge> g = loadFromDB();
        Graph<String, SimpleEdge> simpleGraph = new DefaultDirectedGraph<>(SimpleEdge.class);
        Map<String, DMNVertex> mapping = new HashMap<String, DMNVertex>();
        Map<String, String> reverseMapping = new HashMap<String, String>();

        //Create mapping for simple DNF/Boolean expression variable

        Integer outIdx = 0;
        Integer inpIdx = 0;
        Integer decIdx = 0;

        for (DMNVertex vertex: g.vertexSet()){
            if (vertex.getClass() == Decision.class) {
                mapping.put("d[" + decIdx.toString() + "]", vertex);
                simpleGraph.addVertex("d[" + decIdx.toString() + "]");
                reverseMapping.put(vertex.toString(), "d[" + decIdx.toString() + "]");
                decIdx++;
            }
            
            if (vertex.getClass() == OutputData.class) {
                mapping.put("o[" + outIdx.toString() + "]", vertex);                
                simpleGraph.addVertex("o[" + outIdx.toString() + "]");               
                reverseMapping.put(vertex.toString(), "o[" + outIdx.toString() + "]");
                outIdx++;
            }
            
            if (vertex.getClass() == InputData.class) {
                mapping.put("i[" + inpIdx.toString() + "]", vertex);             
                simpleGraph.addVertex("i[" + inpIdx.toString() + "]");
                reverseMapping.put(vertex.toString(), "i[" + inpIdx.toString() + "]");
                inpIdx++;
            }
        }

        for (DMNEdge edge: g.edgeSet()){
            simpleGraph.addEdge(reverseMapping.get(edge.getSource().toString()), reverseMapping.get(edge.getTarget().toString()));
        }

        //Create a set of output data Os which contains all output data available in the business knowledge repository, which are semantic similar to o.
        //Extend Is with all input data available in the business knowledge repository, which are semantic similar to an element of the initial Is
        DISCO disco = DISCO.load("/home/kuosi/dev/enwiki-20130403-word2vec-lm-mwl-lc-sim.denseMatrix");
        List<String> semanticSimilarOutputs = new ArrayList<String>();
        List<String> semanticSimilarInputs = new ArrayList<String>();
        List<String> simpleSemanticSimilarOutputs = new ArrayList<String>();
        List<String> simpleSemanticSimilarInputs = new ArrayList<String>();
        for (DMNVertex vertex : g.vertexSet()) {

            if (vertex.getClass() == InputData.class) {

                InputData inputData = (InputData) vertex;

                boolean found = false;
                int i = 0;

                while ((!found) && i < userInputData.size()) {
                    String dataName = userInputData.get(i);
                    if (checkSimilarity(disco, dataName, inputData.getName()) >= threshold) {
                        semanticSimilarInputs.add(inputData.getName());
                        simpleSemanticSimilarInputs.add(reverseMapping.get(inputData.toString()));
                        semanticSimilarInputData = semanticSimilarInputData + inputData.getName() + ", ";
                        found = true;
                    }

                    i++;
                }
            }

            if (vertex.getClass() == OutputData.class) {

                OutputData outputData = (OutputData) vertex;

                boolean found = false;
                int i = 0;

                while ((!found) && i < userOutputData.size()) {
                    String dataName = userOutputData.get(i);
                    if (checkSimilarity(disco, dataName, outputData.getName()) >= threshold) {
                        semanticSimilarOutputs.add(outputData.getName());
                        simpleSemanticSimilarOutputs.add(reverseMapping.get(outputData.toString()));
                        semanticSimilarOutputData = semanticSimilarOutputData + outputData.getName() + ", ";
                        found = true;
                    }

                    i++;
                }
            }
        }

        //For each output in the set of semantically similar outputs
        Set<String> simpleMCS = new HashSet<String>();
        for (String topNode: simpleSemanticSimilarOutputs){
            //Build boolean expression
            String expr = booleanExpr(simpleGraph, topNode);

            System.out.println(expr);

            //Create DNF ang get minimal cut sets / minimal tie sets      
            String[] command = new String[] {
                    "python3",
                    "/home/kuosi/dev/master_thesis/06_Implementation/02_boolean_algebra/dnf.py",
                    expr
            };
            String inputLine = "Y";

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            writer.write(inputLine);
            writer.newLine();
            writer.close();

            String line;

            while ((line = reader.readLine()) != null) {
                simpleMCS.add(line);
            }

        }
        
        //Create graph for each minimal cut set if all the input data are provided by the users and all decisions meet the business objectives specified by the user.
        for (String simpleMcs: simpleMCS) {
            Graph<DMNVertex, DMNEdge> newGraph = new DefaultDirectedGraph<>(DMNEdge.class);
            List<DMNVertex> newOutputList = new ArrayList<DMNVertex>();
            List<DMNVertex> newDecisionList = new ArrayList<DMNVertex>();            
            List<DMNVertex> newInputList = new ArrayList<DMNVertex>();
            boolean inputMatch = true;
            boolean objectiveMatch = true;
            String mcs="";
            System.out.println(simpleMcs);
            for (String strNode: simpleMcs.split(" ")){
                //If node in user input data
                if (strNode.startsWith("o")) {
                    newGraph.addVertex(mapping.get(strNode));
                    newOutputList.add(mapping.get(strNode));

                    OutputData dec = (OutputData) mapping.get(strNode);
                    mcs = mcs + dec.getName() + ", ";
                }
                if (strNode.startsWith("i")) {
                    newGraph.addVertex(mapping.get(strNode));
                    if (!simpleSemanticSimilarInputs.contains(strNode)) {
                        inputMatch = false;
                    }
                    newInputList.add(mapping.get(strNode));

                    InputData dec = (InputData) mapping.get(strNode);
                    mcs = mcs + dec.getName() + ", ";
                }
                if (strNode.startsWith("d")) {
                    newGraph.addVertex(mapping.get(strNode));
                    //Check business objective
                    newDecisionList.add(mapping.get(strNode));

                    Decision dec = (Decision) mapping.get(strNode);
                    mcs = mcs + dec.getName() + " decision, ";
                    
                    objectiveMatch = businessObjectiveEvaluation(businessObjectives, dec, threshold, disco);

                    System.out.println(objectiveMatch);
                    
                }
            }
            mcs = "[" + StringUtils.strip(mcs.strip(), ",") + "]";
            MCS.add(mcs);

            if (inputMatch && objectiveMatch) {
                MCSMatchingInputsObjective.add(mcs);
                for (DMNVertex vertex: newOutputList){
                    for (DMNEdge edge: g.incomingEdgesOf(vertex)){ 
                        DMNVertex sourceVertex = edge.getSource();
                        if (newDecisionList.contains(sourceVertex)){
                            newGraph.addEdge(sourceVertex, vertex);
                        }
                    }
                }
                for (DMNVertex vertex: newDecisionList){
                    for (DMNEdge edge: g.incomingEdgesOf(vertex)){ 
                        DMNVertex sourceVertex = edge.getSource();
                        if (newOutputList.contains(sourceVertex) || newInputList.contains(sourceVertex)){
                            newGraph.addEdge(sourceVertex, vertex);
                        }
                    }
                }

                
                reactGraphs.add(convertFromJavaToReact(newGraph));
            }
        }

        ReactResults reactResult = new ReactResults();
        reactResult.setMCS(MCS);
        reactResult.setMCSMatchingInputsObjective(MCSMatchingInputsObjective);
        reactResult.setReactGraphs(reactGraphs);
        reactResult.setSemanticSimilarInputData(StringUtils.strip(semanticSimilarInputData.strip(), ","));
        reactResult.setSemanticSimilarOutputData(StringUtils.strip(semanticSimilarOutputData.strip(), ","));

        return reactResult;
    }
}
