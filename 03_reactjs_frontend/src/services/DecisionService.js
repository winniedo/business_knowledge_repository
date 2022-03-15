import axios from 'axios';

const DECISION_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/decision/";

class DecisionService {

    getDecisions(){
        return axios.get(DECISION_API_BASE_URL);
    }

    createDecision(decision){
        return axios.post(DECISION_API_BASE_URL, decision);
    }

    getDecisionById(decisionId){
        return axios.get(DECISION_API_BASE_URL + 'extended/' + decisionId);
    }

    updateDecision(decision, decisionId){
        return axios.put(DECISION_API_BASE_URL + decisionId, decision);
    }

    addKnowledgeSource(decisionId, knowledgeSourceId){
        return axios.put(DECISION_API_BASE_URL + 'knowledgeSource/?decisionId=' + decisionId + '&knowledgeSourceId=' + knowledgeSourceId);
    }

    addOwner(decisionId, ownerId){
        return axios.put(DECISION_API_BASE_URL + 'owner/?decisionId=' + decisionId + '&ownerId=' + ownerId);
    }

    addPerformanceMeasure(decisionId, decisionMakerId, performanceIndicatorName, rating){
        return axios.put(DECISION_API_BASE_URL + 'performanceMeasure/?decisionId=' + decisionId + '&decisionMakerId=' + decisionMakerId + '&performanceIndicatorName=' + performanceIndicatorName + '&rating=' + rating);
    }

    addBusinessKnowledge(decisionId, businessKnowledgeId){
        return axios.put(DECISION_API_BASE_URL + 'businessKnowledge/?decisionId=' + decisionId + '&businessKnowledgeId=' + businessKnowledgeId);
    }

    addInputData(decisionId, inputDataId){
        return axios.put(DECISION_API_BASE_URL + 'inputData/?decisionId=' + decisionId + '&inputDataId=' + inputDataId);
    }

    addOuputAsInputData(decisionId, ouputAsInputDataId){
        return axios.put(DECISION_API_BASE_URL + 'outputAsInputData/?decisionId=' + decisionId + '&outputAsInputDataId=' + ouputAsInputDataId);
    }

    addOutputData(decisionId, outputDataId){
        return axios.put(DECISION_API_BASE_URL + 'outputData/?decisionId=' + decisionId + '&outputDataId=' + outputDataId);
    }

    deleteDecision(decisionId){
        return axios.delete(DECISION_API_BASE_URL + decisionId);
    }

    removeKnowledgeSource(decisionId, knowledgeSourceId){
        return axios.delete(DECISION_API_BASE_URL + 'knowledgeSource/?decisionId=' + decisionId + '&knowledgeSourceId=' + knowledgeSourceId);
    }

    removeOwner(decisionId, ownerId){
        return axios.delete(DECISION_API_BASE_URL + 'owner/?decisionId=' + decisionId + '&ownerId=' + ownerId);
    }

    removePerformanceMeasure(decisionId, performanceMeasuresId){
        return axios.delete(DECISION_API_BASE_URL + 'performanceMeasure/?decisionId=' + decisionId + '&performanceMeasuresId=' + performanceMeasuresId);
    }

    removePerformanceIndicator(decisionId, performanceIndicatorId){
        return axios.delete(DECISION_API_BASE_URL + 'performancIndicator/?decisionId=' + decisionId + '&performanceIndicatorId=' + performanceIndicatorId);
    }

    removeBusinessKnowledge(decisionId, businessKnowledgeId){
        return axios.delete(DECISION_API_BASE_URL + 'businessKnowledge/?decisionId=' + decisionId + '&businessKnowledgeId=' + businessKnowledgeId);
    }

    removeInputData(decisionId, inputDataId){
        return axios.delete(DECISION_API_BASE_URL + 'inputData/?decisionId=' + decisionId + '&inputDataId=' + inputDataId);
    }

    removeOuputAsInputData(decisionId, ouputAsInputDataId){
        return axios.delete(DECISION_API_BASE_URL + 'outputAsInputData/?decisionId=' + decisionId + '&outputAsInputDataId=' + ouputAsInputDataId);
    }

    removeOutputData(decisionId, outputDataId){
        return axios.delete(DECISION_API_BASE_URL + 'outputData/?decisionId=' + decisionId + '&outputDataId=' + outputDataId);
    }
}

export default new DecisionService()