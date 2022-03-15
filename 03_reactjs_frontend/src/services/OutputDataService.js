import axios from 'axios';

const OUTPUTDATA_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/outputData/";

class OutputDataService {

    getOutputData(){
        return axios.get(OUTPUTDATA_API_BASE_URL);
    }

    createOutputData(outputData){
        return axios.post(OUTPUTDATA_API_BASE_URL, outputData);
    }

    getOutputDataById(outputDataId){
        return axios.get(OUTPUTDATA_API_BASE_URL + outputDataId);
    }

    updateOutputData(outputData, outputDataId){
        return axios.put(OUTPUTDATA_API_BASE_URL + outputDataId, outputData);
    }

    deleteOutputData(outputDataId){
        return axios.delete(OUTPUTDATA_API_BASE_URL + outputDataId);
    }
}

export default new OutputDataService()