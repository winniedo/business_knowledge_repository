import React, { Component } from 'react'
import { withRouter } from 'react-router-dom';
import DecisionService from '../../services/DecisionService'

class ListDecisionComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            decisions: []
        }
        this.addDecision = this.addDecision.bind(this);
        this.deleteDecision = this.deleteDecision.bind(this);
    }

    deleteDecision(id) {
        DecisionService.deleteDecision(id).then(res => {
            this.setState({ decisions: this.state.decisions.filter(decision => decision.id !== id) });
        });
    }
    viewDecision(id) {
        this.props.history.push(`/view-decision/${id}`);
    }

    componentDidMount() {
        DecisionService.getDecisions().then((res) => {
            this.setState({ decisions: res.data });
        });
    }

    addDecision() {
        this.props.history.push('/add-decision/_add');
    }

    render() {
        return (
            <div>
                <div className="row">
                    <button className="btn btn-primary" onClick={this.addDecision}> Add Decision</button>
                </div>
                <br></br>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th> Decision Name</th>
                                <th> Decision Description</th>
                                <th> Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.decisions.map(
                                    decision =>
                                        <tr key={decision.id}>
                                            <td width="20%"> {decision.name} </td>
                                            <td width="50%"> {decision.description}</td>
                                            <td width="30%">
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.deleteDecision(decision.id)} className="btn btn-danger">Delete </button>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.viewDecision(decision.id)} className="btn btn-info">View</button>
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

export default withRouter(ListDecisionComponent)