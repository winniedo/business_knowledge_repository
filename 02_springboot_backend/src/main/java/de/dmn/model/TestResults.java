package de.dmn.model;

public class TestResults {
    Integer numberOfNodes;
    Integer numberOfEdges;
    Integer numberOfInputNodes;
    Integer numberOfOutputNodes;
    Integer numberOfDecisionNodes;
    Long booleanExprTime;
    Long DNF_time;
    Integer numberDNF;
    Long graphBuildingTime;
    public TestResults(Integer numberOfNodes, Integer numberOfEdges, Integer numberOfInputNodes,
            Integer numberOfOutputNodes, Integer numberOfDecisionNodes, Long booleanExprTime, Long dNF_time,
            Integer numberDNF, Long graphBuildingTime) {
        this.numberOfNodes = numberOfNodes;
        this.numberOfEdges = numberOfEdges;
        this.numberOfInputNodes = numberOfInputNodes;
        this.numberOfOutputNodes = numberOfOutputNodes;
        this.numberOfDecisionNodes = numberOfDecisionNodes;
        this.booleanExprTime = booleanExprTime;
        this.DNF_time = dNF_time;
        this.numberDNF = numberDNF;
        this.graphBuildingTime = graphBuildingTime;
    }
    public Integer getNumberOfNodes() {
        return numberOfNodes;
    }
    public void setNumberOfNodes(Integer numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
    public Integer getNumberOfEdges() {
        return numberOfEdges;
    }
    public void setNumberOfEdges(Integer numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }
    public Integer getNumberOfInputNodes() {
        return numberOfInputNodes;
    }
    public void setNumberOfInputNodes(Integer numberOfInputNodes) {
        this.numberOfInputNodes = numberOfInputNodes;
    }
    public Integer getNumberOfOutputNodes() {
        return numberOfOutputNodes;
    }
    public void setNumberOfOutputNodes(Integer numberOfOutputNodes) {
        this.numberOfOutputNodes = numberOfOutputNodes;
    }
    public Integer getNumberOfDecisionNodes() {
        return numberOfDecisionNodes;
    }
    public void setNumberOfDecisionNodes(Integer numberOfDecisionNodes) {
        this.numberOfDecisionNodes = numberOfDecisionNodes;
    }
    public long getBooleanExprTime() {
        return booleanExprTime;
    }
    public void setBooleanExprTime(long booleanExprTime) {
        this.booleanExprTime = booleanExprTime;
    }
    public long getDNF_time() {
        return DNF_time;
    }
    public void setDNF_time(long dNF_time) {
        DNF_time = dNF_time;
    }
    public Integer getNumberDNF() {
        return numberDNF;
    }
    public void setNumberDNF(Integer numberDNF) {
        this.numberDNF = numberDNF;
    }
    public long getGraphBuildingTime() {
        return graphBuildingTime;
    }
    public void setGraphBuildingTime(long graphBuildingTime) {
        this.graphBuildingTime = graphBuildingTime;
    }
    
}
