import React, { Component } from 'react'
import { withRouter } from 'react-router-dom';
import OutputDataService from '../../services/OutputDataService'

class ListOutputDataComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            outputData: []
        }
        this.addOutputData = this.addOutputData.bind(this);
        this.editOutputData = this.editOutputData.bind(this);
        this.deleteOutputData = this.deleteOutputData.bind(this);
    }

    addOutputData() {
        this.props.history.push('/add-outputData/_add');
    }    
    editOutputData(id) {
        this.props.history.push(`/add-outputData/${id}`);
    }
    deleteOutputData(id) {
        OutputDataService.deleteOutputData(id).then(res => {
            this.setState({ outputData: this.state.outputData.filter(outputData => outputData.id !== id) });
        });
    }
    viewOutputData(id) {
        this.props.history.push(`/view-outputData/${id}`);
    }
    componentDidMount() {
        OutputDataService.getOutputData().then((res) => {
            console.log(res.data)
            this.setState({ outputData: res.data });
        });
    }

    render() {
        return (
            <div>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addOutputData}> Add OutputData</button>
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
                                this.state.outputData.map(
                                    outputData =>
                                        <tr key={outputData.id}>
                                            <td width="20%"> {outputData.name} </td>
                                            <td width="50%"> {outputData.description}</td>
                                            <td width="30%">
                                                <button onClick={() => this.editOutputData(outputData.id)} className="btn btn-info">Update </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.deleteOutputData(outputData.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.viewOutputData(outputData.id)} className="btn btn-info">View </button>
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

export default withRouter(ListOutputDataComponent)