import axios from 'axios';

const BUSINESSKNOWLEDGE_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/businessKnowledge/";

class BusinessKnowledgeService {

    getBusinessKnowledge(){
        return axios.get(BUSINESSKNOWLEDGE_API_BASE_URL);
    }

    createBusinessKnowledge(businessKnowledge){
        return axios.post(BUSINESSKNOWLEDGE_API_BASE_URL, businessKnowledge);
    }

    getBusinessKnowledgeById(businessKnowledgeId){
        return axios.get(BUSINESSKNOWLEDGE_API_BASE_URL + businessKnowledgeId);
    }

    updateBusinessKnowledge(businessKnowledge, businessKnowledgeId){
        return axios.put(BUSINESSKNOWLEDGE_API_BASE_URL + businessKnowledgeId, businessKnowledge);
    }

    deleteBusinessKnowledge(businessKnowledgeId){
        return axios.delete(BUSINESSKNOWLEDGE_API_BASE_URL + businessKnowledgeId);
    }
}

export default new BusinessKnowledgeService()