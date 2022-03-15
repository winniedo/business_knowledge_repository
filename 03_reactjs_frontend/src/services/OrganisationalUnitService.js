import axios from 'axios';

const ORGANISATIONALUNIT_API_BASE_URL = "http://192.168.178.44:8080/dmnapi/v1/organisationalUnit/";

class OrganisationalUnitService {

    getOrganisationalUnit(){
        return axios.get(ORGANISATIONALUNIT_API_BASE_URL);
    }

    createOrganisationalUnit(organisationalUnit){
        return axios.post(ORGANISATIONALUNIT_API_BASE_URL, organisationalUnit);
    }

    getOrganisationalUnitById(organisationalUnitId){
        return axios.get(ORGANISATIONALUNIT_API_BASE_URL + organisationalUnitId);
    }

    updateOrganisationalUnit(organisationalUnit, organisationalUnitId){
        return axios.put(ORGANISATIONALUNIT_API_BASE_URL + organisationalUnitId, organisationalUnit);
    }

    deleteOrganisationalUnit(organisationalUnitId){
        return axios.delete(ORGANISATIONALUNIT_API_BASE_URL + organisationalUnitId);
    }
}

export default new OrganisationalUnitService()