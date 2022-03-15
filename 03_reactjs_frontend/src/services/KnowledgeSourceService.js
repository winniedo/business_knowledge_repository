import axios from 'axios';

const KNOWLEDGESOURCE_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/knowledgeSource/";

class KnowledgeSourceService {

    getKnowledgeSource(){
        return axios.get(KNOWLEDGESOURCE_API_BASE_URL);
    }

    createKnowledgeSource(knowledgeSource){
        return axios.post(KNOWLEDGESOURCE_API_BASE_URL, knowledgeSource);
    }

    getKnowledgeSourceById(knowledgeSourceId){
        return axios.get(KNOWLEDGESOURCE_API_BASE_URL + knowledgeSourceId);
    }

    updateKnowledgeSource(knowledgeSource, knowledgeSourceId){
        return axios.put(KNOWLEDGESOURCE_API_BASE_URL + knowledgeSourceId, knowledgeSource);
    }

    deleteKnowledgeSource(knowledgeSourceId){
        return axios.delete(KNOWLEDGESOURCE_API_BASE_URL + knowledgeSourceId);
    }
}

export default new KnowledgeSourceService()