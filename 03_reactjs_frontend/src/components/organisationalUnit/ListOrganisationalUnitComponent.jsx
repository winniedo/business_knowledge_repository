import React, { Component } from 'react'
import { withRouter } from 'react-router-dom';
import OrganisationalUnitService from '../../services/OrganisationalUnitService'

class ListOrganisationalUnitComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            organisationalUnit: []
        }
        this.addOrganisationalUnit = this.addOrganisationalUnit.bind(this);
        this.editOrganisationalUnit = this.editOrganisationalUnit.bind(this);
        this.deleteOrganisationalUnit = this.deleteOrganisationalUnit.bind(this);
    }

    addOrganisationalUnit() {
        this.props.history.push('/add-organisationalUnit/_add');
    }    
    editOrganisationalUnit(id) {
        this.props.history.push(`/add-organisationalUnit/${id}`);
    }
    deleteOrganisationalUnit(id) {
        OrganisationalUnitService.deleteOrganisationalUnit(id).then(res => {
            this.setState({ organisationalUnit: this.state.organisationalUnit.filter(organisationalUnit => organisationalUnit.id !== id) });
        });
    }
    viewOrganisationalUnit(id) {
        this.props.history.push(`/view-organisationalUnit/${id}`);
    }
    componentDidMount() {
        OrganisationalUnitService.getOrganisationalUnit().then((res) => {
            console.log(res.data)
            this.setState({ organisationalUnit: res.data });
        });
    }

    render() {
        return (
            <div>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addOrganisationalUnit}> Add OrganisationalUnit</button>
                </div>
                <br></br>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.organisationalUnit.map(
                                    organisationalUnit =>
                                        <tr key={organisationalUnit.id}>
                                            <td width="20%"> <a href={organisationalUnit.url}>{organisationalUnit.name}</a></td>
                                            <td width="50%"> {organisationalUnit.description}</td>
                                            <td width="30%">
                                                <button onClick={() => this.editOrganisationalUnit(organisationalUnit.id)} className="btn btn-info">Update </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.deleteOrganisationalUnit(organisationalUnit.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.viewOrganisationalUnit(organisationalUnit.id)} className="btn btn-info">View </button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>

            </div>
        )
    }
}

export default withRouter(ListOrganisationalUnitComponent)