package de.dmn.model;

import java.util.List;
import java.util.Set;

public class ReactResults {
    String semanticSimilarOutputData;
    String semanticSimilarInputData;
    List<String> MCS;
    List<String> MCSMatchingInputsObjective;
    List<ReactGraph> reactGraphs;
    public String getSemanticSimilarOutputData() {
        return semanticSimilarOutputData;
    }
    public void setSemanticSimilarOutputData(String semanticSimilarOutputData) {
        this.semanticSimilarOutputData = semanticSimilarOutputData;
    }
    public String getSemanticSimilarInputData() {
        return semanticSimilarInputData;
    }
    public void setSemanticSimilarInputData(String semanticSimilarInputData) {
        this.semanticSimilarInputData = semanticSimilarInputData;
    }
    public List<String> getMCS() {
        return MCS;
    }
    public void setMCS(List<String> mCS) {
        MCS = mCS;
    }
    public List<String> getMCSMatchingInputsObjective() {
        return MCSMatchingInputsObjective;
    }
    public void setMCSMatchingInputsObjective(List<String> mCSMatchingInputsObjective) {
        MCSMatchingInputsObjective = mCSMatchingInputsObjective;
    }
    public List<ReactGraph> getReactGraphs() {
        return reactGraphs;
    }
    public void setReactGraphs(List<ReactGraph> reactGraphs) {
        this.reactGraphs = reactGraphs;
    }

    
}
