import axios from 'axios';

const INPUTDATA_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/inputData/";

class InputDataService {

    getInputData(){
        return axios.get(INPUTDATA_API_BASE_URL);
    }

    createInputData(inputData){
        return axios.post(INPUTDATA_API_BASE_URL, inputData);
    }

    getInputDataById(inputDataId){
        return axios.get(INPUTDATA_API_BASE_URL + inputDataId);
    }

    updateInputData(inputData, inputDataId){
        return axios.put(INPUTDATA_API_BASE_URL + inputDataId, inputData);
    }

    deleteInputData(inputDataId){
        return axios.delete(INPUTDATA_API_BASE_URL + inputDataId);
    }
}

export default new InputDataService()