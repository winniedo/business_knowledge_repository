package de.dmn.model;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class DecisionExtended {

    private long id;
    private String name;
    private String description;
    private List<KnowledgeSource> knowledgeSources;
    private List<KnowledgeSource> notKnowledgeSources;
    private List<OrganisationalUnit> owners;
    private List<OrganisationalUnit> notOwners;
    private List<PerformanceIndicator> performanceIndicators;
    private List<PerformanceIndicator> allPerformanceIndicators;
    private List<BusinessKnowledgeModel> businessKnowledges;
    private List<BusinessKnowledgeModel> notBusinessKnowledges;
    private List<InputData> inputData;
    private List<InputData> notInputData;
    private List<OutputData> outputAsInputData;
    private List<OutputData> notOutputAsInputData;
    private List<OutputData> outputData;
    private List<OutputData> notOutputData;
    private List<OrganisationalUnit> allDecisionMakers;
    

    public DecisionExtended() {

    }

    public DecisionExtended(Decision decision) {
        this.id = decision.getId();
        this.name = decision.getName();
        this.description = decision.getDescription();
        this.knowledgeSources = decision.getKnowledgeSources();
        this.owners = decision.getOwners();
        this.performanceIndicators = decision.getPerformanceIndicators();
        this.businessKnowledges = decision.getBusinessKnowledges();
        this.inputData = decision.getInputData();
        this.outputAsInputData = decision.getOutputAsInputData();
        this.outputData = decision.getOutputData();
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

    public List<KnowledgeSource> getNotKnowledgeSources() {
        return notKnowledgeSources;
    }

    public void setNotKnowledgeSources(List<KnowledgeSource> notKnowledgeSources) {
        this.notKnowledgeSources = notKnowledgeSources;
    }

    public List<OrganisationalUnit> getOwners() {
        return owners;
    }

    public void setOwners(List<OrganisationalUnit> owners) {
        this.owners = owners;
    }

    public List<OrganisationalUnit> getNotOwners() {
        return notOwners;
    }

    public void setNotOwners(List<OrganisationalUnit> notOwners) {
        this.notOwners = notOwners;
    }

    public List<PerformanceIndicator> getPerformanceIndicators() {
        return performanceIndicators;
    }

    public void setPerformanceIndicators(List<PerformanceIndicator> performanceIndicators) {
        this.performanceIndicators = performanceIndicators;
    }

    public List<PerformanceIndicator> getAllPerformanceIndicators() {
        return allPerformanceIndicators;
    }

    public void setAllPerformanceIndicators(List<PerformanceIndicator> allPerformanceIndicators) {                
        Set<PerformanceIndicator> set = new HashSet<>(allPerformanceIndicators);
        allPerformanceIndicators.clear();
        allPerformanceIndicators.addAll(set);
        this.allPerformanceIndicators = allPerformanceIndicators;
    }

    public List<BusinessKnowledgeModel> getBusinessKnowledges() {
        return businessKnowledges;
    }

    public void setBusinessKnowledges(List<BusinessKnowledgeModel> businessKnowledges) {
        this.businessKnowledges = businessKnowledges;
    }

    public List<BusinessKnowledgeModel> getNotBusinessKnowledges() {
        return notBusinessKnowledges;
    }

    public void setNotBusinessKnowledges(List<BusinessKnowledgeModel> notBusinessKnowledges) {
        this.notBusinessKnowledges = notBusinessKnowledges;
    }

    public List<InputData> getInputData() {
        return inputData;
    }

    public void setInputData(List<InputData> inputData) {
        this.inputData = inputData;
    }

    public List<InputData> getNotInputData() {
        return notInputData;
    }

    public void setNotInputData(List<InputData> notInputData) {
        this.notInputData = notInputData;
    }

    public List<OutputData> getOutputData() {
        return outputData;
    }

    public void setOutputData(List<OutputData> outputData) {
        this.outputData = outputData;
    }

    public List<OutputData> getNotOutputData() {
        return notOutputData;
    }

    public void setNotOutputData(List<OutputData> notOutputData) {
        this.notOutputData = notOutputData;
    }

    public List<OrganisationalUnit> getAllDecisionMakers() {
        return allDecisionMakers;
    }

    public void setAllDecisionMakers(List<OrganisationalUnit> allDecisionMakers) {
        this.allDecisionMakers = allDecisionMakers;
    }

    public List<OutputData> getOutputAsInputData() {
        return outputAsInputData;
    }

    public void setOutputAsInputData(List<OutputData> outputAsInputData) {
        this.outputAsInputData = outputAsInputData;
    }

    public List<OutputData> getNotOutputAsInputData() {
        return notOutputAsInputData;
    }

    public void setNotOutputAsInputData(List<OutputData> notOutputAsInputData) {
        this.notOutputAsInputData = notOutputAsInputData;
    }

    
}
