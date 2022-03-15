package de.dmn.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import de.dmn.service.graph.DMNVertex;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity
@Table(name = "decision")
public class Decision extends DMNVertex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "DECISIONS_KNOWLEDGESOURCES",
        joinColumns = @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "KNOWLEDGESOURCE_ID", referencedColumnName = "ID")
    )
    private List<KnowledgeSource> knowledgeSources;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "DECISIONS_ORGANISATIONALUNITS",
        joinColumns = @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ORGANISATIONALUNIT_ID", referencedColumnName = "ID")
    )
    private List<OrganisationalUnit> owners;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID")
    private List<PerformanceIndicator> performanceIndicators;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "DECISIONS_BUSINESSKNOWLEDGES",
        joinColumns = @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "BUSINESSKNOWLEDGE_ID", referencedColumnName = "ID")
    )
    private List<BusinessKnowledgeModel> businessKnowledges;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "DECISIONS_INPUTDATA",
        joinColumns = @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "INPUTDATA_ID", referencedColumnName = "ID")
    )
    private List<InputData> inputData;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "DECISIONS_OUTPUTASINPUTDATA",
        joinColumns = @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "OUTPUTASINPUTDATA", referencedColumnName = "ID")
    )
    private List<OutputData> outputAsInputData;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "DECISIONS_OUTPUTDATA",
        joinColumns = @JoinColumn(name = "DECISION_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "OUTPUTDATA_ID", referencedColumnName = "ID")
    )
    private List<OutputData> outputData;
    

    public Decision() {

    }

    public Decision(String name, String description) {
        this.name = name;
        this.description = description;
        this.knowledgeSources = new ArrayList<KnowledgeSource>();
        this.owners = new ArrayList<OrganisationalUnit>(); 
        this.performanceIndicators = new ArrayList<PerformanceIndicator>();
        this.businessKnowledges = new ArrayList<BusinessKnowledgeModel>();
        this.inputData = new ArrayList<InputData>();
        this.outputData = new ArrayList<OutputData>();
    }

    @Override
    public String toString() {
        return "decision" + id + "_" + name;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Decision) && (toString().equals(o.toString()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<KnowledgeSource> getKnowledgeSources() {
        return knowledgeSources;
    }

    public void setKnowledgeSources(List<KnowledgeSource> knowledgeSources) {
        this.knowledgeSources = knowledgeSources;
    }

    public void addKnowledgeSource(KnowledgeSource knowledgeSource) {
        this.knowledgeSources.add(knowledgeSource);
    }

    public void removeKnowledgeSource(KnowledgeSource knowledgeSource) {
        this.knowledgeSources.remove(knowledgeSource);
    }

    public List<OrganisationalUnit> getOwners() {
        return owners;
    }

    public void setOwners(List<OrganisationalUnit> owners) {
        this.owners = owners;
    }

    public void addOwner(OrganisationalUnit owner) {
        this.owners.add(owner);
    }

    public void removeOwner(OrganisationalUnit owner) {
        this.owners.remove(owner);
    }

    public List<PerformanceIndicator> getPerformanceIndicators() {
        return performanceIndicators;
    }

    public void setPerformanceIndicators(List<PerformanceIndicator> performanceIndicators) {
        this.performanceIndicators = performanceIndicators;
    }

    public void addPerformanceIndicator(PerformanceIndicator performanceIndicator) {
        this.performanceIndicators.add(performanceIndicator);
    }

    public void removePerformanceIndicator(PerformanceIndicator performanceIndicator) {
        this.performanceIndicators.remove(performanceIndicator);
    }

    public List<BusinessKnowledgeModel> getBusinessKnowledges() {
        return businessKnowledges;
    }

    public void setBusinessKnowledges(List<BusinessKnowledgeModel> businessKnowledges) {
        this.businessKnowledges = businessKnowledges;
    }    

    public void addBusinessKnowledge(BusinessKnowledgeModel businessKnowledge) {
        this.businessKnowledges.add(businessKnowledge);
    }

    public void removeBusinessKnowledge(BusinessKnowledgeModel businessKnowledge) {
        this.businessKnowledges.remove(businessKnowledge);
    }

    public List<InputData> getInputData() {
        return inputData;
    }

    public void setInputData(List<InputData> inputData) {
        this.inputData = inputData;
    }

    public void addInputData(InputData inputData) {
        this.inputData.add(inputData);
    }

    public void removeInputData(InputData inputData) {
        this.inputData.remove(inputData);
    }    

    public List<OutputData> getOutputAsInputData() {
        return outputAsInputData;
    }

    public void setOutputAsInputData(List<OutputData> outputAsInputData) {
        this.outputAsInputData = outputAsInputData;
    }    

    public void addOutputAsInputData(OutputData outputAsInputData) {
        this.outputAsInputData.add(outputAsInputData);
    }

    public void removeOutputAsInputData(OutputData outputAsInputData) {
        this.outputAsInputData.remove(outputAsInputData);
    }

    public List<OutputData> getOutputData() {
        return outputData;
    }

    public void setOutputData(List<OutputData> outputData) {
        this.outputData = outputData;
    }    

    public void addOutputData(OutputData outputData) {
        this.outputData.add(outputData);
    }

    public void removeOutputData(OutputData outputData) {
        this.outputData.remove(outputData);
    }

    public void updateCombinedRatings() {        
        for (PerformanceIndicator performanceIndicator: this.getPerformanceIndicators()){
            double intermediateCombinedRating = 0.0;
            for (PerformanceMeasure performanceMeasure2: performanceIndicator.getPerformanceMeasures()) {
                intermediateCombinedRating = intermediateCombinedRating + performanceMeasure2.getRating();
            }
            if (performanceIndicator.getPerformanceMeasures().size() > 0) {
                intermediateCombinedRating = intermediateCombinedRating / performanceIndicator.getPerformanceMeasures().size();
            }
            performanceIndicator.setCombinedRating(intermediateCombinedRating);
        }        
    }
}
