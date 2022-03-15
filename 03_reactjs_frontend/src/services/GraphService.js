import axios from 'axios';

const GRAPH_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/graph/";

class GraphService {

    getAllInputData(){
        return axios.get(GRAPH_API_BASE_URL + "allInputData/");
    }

    getAllOutputData(){
        return axios.get(GRAPH_API_BASE_URL + "allOutputData/");
    }

    getInitialGraph(){
        return axios.get(GRAPH_API_BASE_URL + "initialGraph/");
    }

    matching(userData, threshold) {
        console.log("here2")
        return axios.post(GRAPH_API_BASE_URL + "matchingAlgorithm/?threshold=" + threshold, userData);
    }

}

export default new GraphService()