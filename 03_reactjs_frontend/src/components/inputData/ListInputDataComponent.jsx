import React, { Component } from 'react'
import { withRouter } from 'react-router-dom';
import InputDataService from '../../services/InputDataService'

class ListInputDataComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            inputData: []
        }
        this.addInputData = this.addInputData.bind(this);
        this.editInputData = this.editInputData.bind(this);
        this.deleteInputData = this.deleteInputData.bind(this);
    }

    addInputData() {
        this.props.history.push('/add-inputData/_add');
    }    
    editInputData(id) {
        this.props.history.push(`/add-inputData/${id}`);
    }
    deleteInputData(id) {
        InputDataService.deleteInputData(id).then(res => {
            this.setState({ inputData: this.state.inputData.filter(inputData => inputData.id !== id) });
        });
    }
    viewInputData(id) {
        this.props.history.push(`/view-inputData/${id}`);
    }
    componentDidMount() {
        InputDataService.getInputData().then((res) => {
            console.log(res.data)
            this.setState({ inputData: res.data });
        });
    }

    render() {
        return (
            <div>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addInputData}> Add InputData</button>
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
                                this.state.inputData.map(
                                    inputData =>
                                        <tr key={inputData.id}>
                                            <td width="20%"> {inputData.name} </td>
                                            <td width="50%"> {inputData.description}</td>
                                            <td width="30%">
                                                <button onClick={() => this.editInputData(inputData.id)} className="btn btn-info">Update </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.deleteInputData(inputData.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.viewInputData(inputData.id)} className="btn btn-info">View </button>
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

export default withRouter(ListInputDataComponent)