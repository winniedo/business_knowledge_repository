package com.upb;

import java.util.ArrayList;
import java.util.List;

public class DMNVertex {
    private DMNVertexType vertexType;
    private String vertexName;
    private String description;
    private KnowledgeSource knowledgeSource;
    private BusinessKnowledgeModel businessKnowledge;
    private OrganisationalUnit owner;
    private List<PerformanceIndicator> performanceIndicators;
    private List<PerformanceMeasure> performanceMeasures;

    public DMNVertex(DMNVertexType vertexType, String vertexName) {
        this.vertexType = vertexType;
        this.vertexName = vertexName;
        performanceIndicators = new ArrayList<PerformanceIndicator>();
    }

    @Override
    public String toString() {
        return vertexType + ": " + vertexName;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DMNVertex) && (toString().equals(o.toString()));
    }

    public DMNVertexType getVertexType() {
        return vertexType;
    }

    public void setVertexType(DMNVertexType vertexType) {
        this.vertexType = vertexType;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public KnowledgeSource getKnowledgeSource() {
        return knowledgeSource;
    }

    public void setKnowledgeSource(KnowledgeSource knowledgeSource) {
        this.knowledgeSource = knowledgeSource;
    }

    public BusinessKnowledgeModel getBusinessKnowledge() {
        return businessKnowledge;
    }

    public void setBusinessKnowledge(BusinessKnowledgeModel businessKnowledge) {
        this.businessKnowledge = businessKnowledge;
    }

    public OrganisationalUnit getOwner() {
        return owner;
    }

    public void setOwner(OrganisationalUnit owner) {
        this.owner = owner;
    }

    public List<PerformanceIndicator> getPerformanceIndicators() {
        return performanceIndicators;
    }

    public void setPerformanceIndicators(List<PerformanceIndicator> performanceIndicators) {
        this.performanceIndicators = performanceIndicators;
    }    

    public List<PerformanceMeasure> getPerformanceMeasures() {
        return performanceMeasures;
    }

    public void setPerformanceMeasures(List<PerformanceMeasure> performanceMeasures) {
        this.performanceMeasures = performanceMeasures;
    }

    public void addPerformanceIndicator(PerformanceIndicator performanceIndicator) {
        performanceIndicators.add(performanceIndicator);
    }

    public void addPerformanceMeasures(PerformanceMeasure performanceMeasure) {
        performanceMeasures.add(performanceMeasure);
    }

}
