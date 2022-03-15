import React, { Component } from 'react'
import Collapsible from 'react-collapsible';
import DecisionService from '../../services/DecisionService'

class ViewDecisionComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            decision: {},
            newPerformanceMeasureName: '',
            newPerformanceMeasureDecisionMakerId: -1,
            newPerformanceMeasureRating: 0,
            inputDataId: -1,
            outputDataId: -1,
            knowledgeSourceId: -1,
            ownerId: -1,
            businessKnowledgeId: -1,
            performanceIndicators: [],
            allPerformanceIndicators: [],
            allDecisionMakers: [],
            inputData: [],
            notInputData: [],
            outputAsInputData: [],
            notOutputAsInputData: [],
            outputData: [],
            notOutputData: [],
            knowledgeSources: [],
            notKnowledgeSources: [],
            owners: [],
            notOwners: [],
            businessKnowledges: [],
            notBusinessKnowledges: []
        }
    }

    refreshState() {
        DecisionService.getDecisionById(this.state.id).then(res => {
            //console.log(res)
            this.setState({
                decision: res.data,
                newPerformanceMeasureName: '',
                newPerformanceMeasureDecisionMakerId: -1,
                newPerformanceMeasureRating: 0,
                inputDataId: -1,
                outputDataId: -1,
                knowledgeSourceId: -1,
                ownerId: -1,
                businessKnowledgeId: -1,
                performanceIndicators: res.data.performanceIndicators,
                allPerformanceIndicators: res.data.allPerformanceIndicators,
                allDecisionMakers: res.data.allDecisionMakers,
                inputData: res.data.inputData,
                notInputData: res.data.notInputData,
                outputAsInputData: res.data.outputAsInputData,
                notOutputAsInputData: res.data.notOutputAsInputData,
                outputData: res.data.outputData,
                notOutputData: res.data.notOutputData,
                knowledgeSources: res.data.knowledgeSources,
                notKnowledgeSources: res.data.notKnowledgeSources,
                owners: res.data.owners,
                notOwners: res.data.notOwners,
                businessKnowledges: res.data.businessKnowledges,
                notBusinessKnowledges: res.data.notBusinessKnowledges
            });
        })
    }

    editDecision(id) {
        this.props.history.push(`/add-decision/${id}`);
    }

    componentDidMount() {
        this.refreshState()
    }

    changePerformanceMeasureNameHandler = (event) => {
        this.setState({ newPerformanceMeasureName: event.target.value });
    }
    changePerformanceMeasureDecisionMakerIdHandler = (event) => {
        this.setState({ newPerformanceMeasureDecisionMakerId: event.target.value });
    }
    changePerformanceMeasureRatingHandler = (event) => {
        this.setState({ newPerformanceMeasureRating: event.target.value });
    }

    changeInputDataIdHandler = (event) => {
        this.setState({ inputDataId: event.target.value });
    }
    changeOutputDataIdHandler = (event) => {
        this.setState({ outputDataId: event.target.value });
    }
    changeKnowledgeSourceIdHandler = (event) => {
        this.setState({ knowledgeSourceId: event.target.value });
    }
    changeOwnerIdHandler = (event) => {
        this.setState({ ownerId: event.target.value });
    }
    changeBusinessKnowledgeIdHandler = (event) => {
        this.setState({ businessKnowledgeId: event.target.value });
    }

    addPerformanceMeasure = (e) => {
        //e.preventDefault();

        console.log("decisionId: " + this.state.decision.id);
        console.log("newPerformanceMeasureName: " + this.state.newPerformanceMeasureName);
        console.log("newPerformanceMeasureDecisionMakerId: " + this.state.newPerformanceMeasureDecisionMakerId);
        console.log("newPerformanceMeasureRating: " + this.state.newPerformanceMeasureRating);

        DecisionService.addPerformanceMeasure(this.state.decision.id, this.state.newPerformanceMeasureDecisionMakerId, this.state.newPerformanceMeasureName, this.state.newPerformanceMeasureRating).then(res => {
            this.refreshState();
        });

    }
    removePerformanceMeasure(decisionId, performanceMeasuresId) {
        DecisionService.removePerformanceMeasure(decisionId, performanceMeasuresId).then(res => {
            this.refreshState();
        });
    }
    removePerformanceIndicator(decisionId, performanceIndicatorId) {
        DecisionService.removePerformanceIndicator(decisionId, performanceIndicatorId).then(res => {
            this.refreshState();
        });
    }

    addKnowledgeSource(decisionId, knowledgeSourceId) {
        DecisionService.addKnowledgeSource(decisionId, knowledgeSourceId).then(res => {
            this.refreshState();
        });
    }

    addOwner(decisionId, ownerId) {
        DecisionService.addOwner(decisionId, ownerId).then(res => {
            this.refreshState();
        });
    }

    addBusinessKnowledge(decisionId, businessKnowledgeId) {
        DecisionService.addBusinessKnowledge(decisionId, businessKnowledgeId).then(res => {
            this.refreshState();
        });
    }

    addInputData(decisionId, id) {
        if (id.substring(0, 1) == "i") {
            DecisionService.addInputData(decisionId, parseInt(id.substring(1))).then(res => {
                this.refreshState();
            });
        } else {
            DecisionService.addOuputAsInputData(decisionId, parseInt(id.substring(1))).then(res => {
                this.refreshState();
            });
        }
    }

    addOutputData(decisionId, outputDataId) {
        DecisionService.addOutputData(decisionId, outputDataId).then(res => {
            this.refreshState();
        });
    }

    removeKnowledgeSource(decisionId, knowledgeSourceId) {
        DecisionService.removeKnowledgeSource(decisionId, knowledgeSourceId).then(res => {
            this.refreshState();
        });
    }

    removeOwner(decisionId, ownerId) {
        DecisionService.removeOwner(decisionId, ownerId).then(res => {
            this.refreshState();
        });
    }

    removeBusinessKnowledge(decisionId, businessKnowledgeId) {
        DecisionService.removeBusinessKnowledge(decisionId, businessKnowledgeId).then(res => {
            this.refreshState();
        });
    }

    removeInputData(decisionId, id) {
        DecisionService.removeInputData(decisionId, id).then(res => {
            this.refreshState();
        });
    }

    removeOutputAsInputData(decisionId, id) {
        DecisionService.removeOuputAsInputData(decisionId, id).then(res => {
            this.refreshState();
        });
    }

removeOutputData(decisionId, outputDataId) {
            DecisionService.removeOutputData(decisionId, outputDataId).then(res => {
                this.refreshState();
            });
        }

render() {
            return(
        <div>
            <br></br>
            <div className="card col-md-6 offset-md-3">
                <h3 className="text-center"> View Decision Details</h3>
                <div className="card-body">
                    <div className="row">
                        <label> Name: </label>
                        <div>{this.state.decision.name}</div>
                    </div>
                    <div className="row">
                        <label> Description: </label>
                        <div> {this.state.decision.description}</div>
                    </div>
                    <div>
                        <button style={{ marginLeft: "10px" }} onClick={() => this.editDecision(this.state.decision.id)} className="btn btn-info">Update </button>
                    </div>
                </div>

            </div>
            <Collapsible trigger="Business objective measures">
                <div className="form-group">
                    <label> Metric: </label>
                    <input type="text" list="performanceIndicatorList" placeholder="Enter business objective name" className="form-control" value={this.state.newPerformanceMeasureName} onChange={this.changePerformanceMeasureNameHandler} />
                    <datalist id='performanceIndicatorList'>
                        {
                            this.state.allPerformanceIndicators.map(
                                performanceIndicator =>
                                    <option key={performanceIndicator.name}>{performanceIndicator.name}</option>
                            )
                        }
                    </datalist>
                </div>
                <div className="form-group">
                    <label> Decision maker: </label>
                    <select className="form-control" value={this.state.newPerformanceMeasureDecisionMakerId} onChange={this.changePerformanceMeasureDecisionMakerIdHandler}>
                        <option key="0" value="-1"></option>
                        {
                            this.state.allDecisionMakers.map(
                                decisionMaker =>
                                    <option key={decisionMaker.id} value={decisionMaker.id}>{decisionMaker.name}</option>
                            )
                        }
                    </select>
                </div>
                <div className="form-group">
                    <label> Rating: </label>
                    <input type="number" placeholder="Enter the rating" className="form-control" value={this.state.newPerformanceMeasureRating} onChange={this.changePerformanceMeasureRatingHandler}></input>
                </div>
                <button className="btn btn-success" onClick={this.addPerformanceMeasure}>Add rating of business objective measure to decision</button>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Combined rating</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.performanceIndicators.map(
                                    performanceIndicator =>
                                        <tr key={performanceIndicator.id}>
                                            <td> {performanceIndicator.name} </td>
                                            <td> {performanceIndicator.combinedRating}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} className="btn btn-danger" onClick={() => this.removePerformanceIndicator(this.state.decision.id, performanceIndicator.id)}>Remove</button>
                                            </td>
                                            <td>
                                                <Collapsible trigger="Individual ratings">
                                                    <table className="table table-striped table-bordered">

                                                        <thead>
                                                            <tr>
                                                                <th>Decision maker</th>
                                                                <th>Rating</th>
                                                                <th>Date</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            {
                                                                performanceIndicator.performanceMeasures.map(
                                                                    performanceMeasure =>
                                                                        <tr key={performanceMeasure.id}>
                                                                            <td> {performanceMeasure.decisionMaker.name} </td>
                                                                            <td> {performanceMeasure.rating}</td>
                                                                            <td> {performanceMeasure.datetime}</td>
                                                                            <td>
                                                                                <button style={{ marginLeft: "10px" }} className="btn btn-danger" onClick={() => this.removePerformanceMeasure(this.state.decision.id, performanceMeasure.id)}>Remove</button>
                                                                            </td>
                                                                        </tr>
                                                                )
                                                            }
                                                        </tbody>
                                                    </table>
                                                </Collapsible>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </Collapsible>
            <Collapsible trigger="Input data">
                <div className="form-group">
                    <select className="form-control" value={this.state.inputDataId} onChange={this.changeInputDataIdHandler}>
                        <option key="0" value="-1"></option>
                        {
                            this.state.notInputData.map(
                                inputData =>
                                    <option key={'i' + inputData.id} value={'i' + inputData.id}>{'Input data: ' + inputData.name}</option>
                            )
                        }
                        {
                            this.state.notOutputAsInputData.map(
                                outputAsInputData =>
                                    <option key={'o' + outputAsInputData.id} value={'o' + outputAsInputData.id}>{'Decision output: ' + outputAsInputData.name}</option>
                            )
                        }
                    </select>
                </div>
                <button className="btn btn-success" onClick={() => this.addInputData(this.state.decision.id, this.state.inputDataId)}>Add input data</button>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.inputData.map(
                                    inputData =>
                                        <tr key={'i' + inputData.id}>
                                            <td> {inputData.name} </td>
                                            <td> {inputData.description}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeInputData(this.state.decision.id, inputData.id)} className="btn btn-danger">Remove</button>
                                            </td>
                                        </tr>
                                )
                            }
                            {
                                this.state.outputAsInputData.map(
                                    outputAsInputData =>
                                        <tr key={'o' + outputAsInputData.id}>
                                            <td> {outputAsInputData.name} </td>
                                            <td> {outputAsInputData.description}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeOutputAsInputData(this.state.decision.id, outputAsInputData.id)} className="btn btn-danger">Remove</button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </Collapsible>
            <Collapsible trigger="Output data">
                <div className="form-group">
                    <select className="form-control" value={this.state.outputDataId} onChange={this.changeOutputDataIdHandler}>
                        <option key="0" value="-1"></option>
                        {
                            this.state.notOutputData.map(
                                outputData =>
                                    <option key={outputData.id} value={outputData.id}>{outputData.name}</option>
                            )
                        }
                    </select>
                </div>
                <button className="btn btn-success" onClick={() => this.addOutputData(this.state.decision.id, this.state.outputDataId)}>Add output data</button>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.outputData.map(
                                    outputData =>
                                        <tr key={outputData.id}>
                                            <td> {outputData.name} </td>
                                            <td> {outputData.description}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeOutputData(this.state.decision.id, outputData.id)} className="btn btn-danger">Remove</button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </Collapsible>
            <Collapsible trigger="Business knowledge">
                <div className="form-group">
                    <select className="form-control" value={this.state.businessKnowledgeId} onChange={this.changeBusinessKnowledgeIdHandler}>
                        <option key="0" value="-1"></option>
                        {
                            this.state.notBusinessKnowledges.map(
                                businessKnowledge =>
                                    <option key={businessKnowledge.id} value={businessKnowledge.id}>{businessKnowledge.name}</option>
                            )
                        }
                    </select>
                </div>
                <button className="btn btn-success" onClick={() => this.addBusinessKnowledge(this.state.decision.id, this.state.businessKnowledgeId)}>Add business knowledge</button>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.businessKnowledges.map(
                                    businessKnowledge =>
                                        <tr key={businessKnowledge.id}>
                                            <td> {businessKnowledge.name} </td>
                                            <td> {businessKnowledge.description}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeBusinessKnowledge(this.state.decision.id, businessKnowledge.id)} className="btn btn-danger">Remove</button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </Collapsible>
            <Collapsible trigger="Knowledge sources">
                <div className="form-group">
                    <select className="form-control" value={this.state.knowledgeSourceId} onChange={this.changeKnowledgeSourceIdHandler}>
                        <option key="0" value="-1"></option>
                        {
                            this.state.notKnowledgeSources.map(
                                knowledgeSource =>
                                    <option key={knowledgeSource.id} value={knowledgeSource.id}>{knowledgeSource.name}</option>
                            )
                        }
                    </select>
                </div>
                <button className="btn btn-success" onClick={() => this.addKnowledgeSource(this.state.decision.id, this.state.knowledgeSourceId)}>Add knowledge source</button>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.knowledgeSources.map(
                                    knowledgeSource =>
                                        <tr key={knowledgeSource.id}>
                                            <td> {knowledgeSource.name} </td>
                                            <td> {knowledgeSource.description}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeKnowledgeSource(this.state.decision.id, knowledgeSource.id)} className="btn btn-danger">Remove</button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </Collapsible>
            <Collapsible trigger="Owners">
                <div className="form-group">
                    <select className="form-control" value={this.state.ownerId} onChange={this.changeOwnerIdHandler}>
                        <option key="0" value="-1"></option>
                        {
                            this.state.notOwners.map(
                                owner =>
                                    <option key={owner.id} value={owner.id}>{owner.name}</option>
                            )
                        }
                    </select>
                </div>
                <button className="btn btn-success" onClick={() => this.addOwner(this.state.decision.id, this.state.ownerId)}>Add owner</button>
                <div className="row">
                    <table className="table table-striped table-bordered">

                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.owners.map(
                                    owner =>
                                        <tr key={owner.id}>
                                            <td> {owner.name} </td>
                                            <td> {owner.description}</td>
                                            <td>
                                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeOwner(this.state.decision.id, owner.id)} className="btn btn-danger">Remove</button>
                                            </td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </Collapsible>
        </div >
    )
    }
}

export default ViewDecisionComponent