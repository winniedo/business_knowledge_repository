import React, { Component } from 'react'
import GraphService from '../../services/GraphService';
import Select from 'react-select'
import ReactDOM from "react-dom";
import * as d3 from "d3";
import dagreD3 from "dagre-d3";

import "./layouting.css";

class Graph extends Component {
    constructor(props) {
        super(props)

        this.state = {
            newInputData: '',
            allInputData: [],
            selectedInputData: [],
            inputDataToBeDeleted: [],

            newOutputData: '',
            allOutputData: [],
            selectedOutputData: [],
            outputDataToBeDeleted: [],

            businessObjective: '',
            selectedBusinessObjective: [],
            businessObjectiveToBeDeleted: [],

            similarityCheckTreshold: 0.7,

            graph: {},
            graphCache: {},
            sessionId: "",

            matchedUserData: {},

            idx: 0,

            listOfGraphs: [],

            result: [],

            iterator: ""

        }
    }

    makeid(length) {
        var result = '';
        var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var charactersLength = characters.length;
        for (var i = 0; i < length; i++) {
            result += characters.charAt(Math.floor(Math.random() *
                charactersLength));
        }
        return result;
    }

    showGraph(data) {
        // Create the input graph
        var g = new dagreD3.graphlib.Graph({ compound: true })
            .setGraph({})
            .setDefaultEdgeLabel(function () {
                return {};
            });

        // Here we're setting the nodes


        data.nodes.forEach(node => {
            //console.log(node.id)
            var color = "SandyBrown";
            if (node.type === "input") {
                color = "LightCyan";
            } else if (node.type === "output") {
                color = "PaleGreen";
            }
            if (node.missing === "x") {
                color = "LightCoral"
            }
            g.setNode(node.type + node.id, { label: node.name, style: "fill: " + color });
        });

        data.edges.forEach(edge => {
            //console.log(node.id)
            var arrow = "vee";
            if (edge.type === "informationItem") {
                arrow = "normal";
            }
            g.setEdge(edge.source.type + edge.source.id, edge.destination.type + edge.destination.id, { arrowhead: arrow });
        });

        g.nodes().forEach(function (v) {
            var node = g.node(v);
            // Round the corners of the nodes
            //console.log(node)
            node.rx = node.ry = 5;
        });

        // Create the renderer
        var render = new dagreD3.render();

        // Set up an SVG group so that we can translate the final graph.
        var svg = d3.select("svg"),
            svgGroup = svg.append("g");

        // Run the renderer. This is what draws the final graph.
        render(d3.select("#svg-canvas"), g);

        /*// Center the graph
        console.log(svg.attr("width"));
        var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
        console.log(xCenterOffset);
        svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
        svg.attr("height", g.graph().height + 40);*/
    }

    refreshState() {
        GraphService.getAllInputData().then(res => {
            this.setState({
                allInputData: res.data
            });
        })
        GraphService.getAllOutputData().then(res => {
            this.setState({
                allOutputData: res.data
            });
        })
        GraphService.getInitialGraph().then(res => {
            this.setState({
                graph: res.data
            });
            //console.log(this.state.graph);
            this.showGraph(this.state.graph)
        });

    }

    componentDidMount() {
        this.refreshState()
        this.setState({
            sessionId: this.makeid(5) + Date.now()
        })
        //console.log("##############");
        //console.log(this.state.initialGraph);
    }

    changeNewInputDataHandler = (event) => {
        this.setState({ newInputData: event.target.value });
    }

    changeNewOutputDataHandler = (event) => {
        this.setState({ newOutputData: event.target.value });
    }

    changeBusinessObjectiveHandler = (event) => {
        this.setState({ businessObjective: event.target.value });
    }

    changeInputDataSelectHandler = (event) => {
        let value = Array.from(event.target.selectedOptions, option => option.value);
        this.setState({ inputDataToBeDeleted: value });
    }

    changeOutputDataSelectHandler = (event) => {
        let value = Array.from(event.target.selectedOptions, option => option.value);
        this.setState({ outputDataToBeDeleted: value });
    }

    changeBusinessObjectiveSelectHandler = (event) => {
        let value = Array.from(event.target.selectedOptions, option => option.value);
        this.setState({ businessObjectiveToBeDeleted: value });
    }

    addInputData() {
        var data = this.state.newInputData.trim();
        if (data !== "") {
            if (!(this.state.selectedInputData.includes(data))) {
                this.state.selectedInputData.push(this.state.newInputData)
            }
            this.setState({ 
                selectedInputData: this.state.selectedInputData,
                newInputData: "" 
            })
        }
    }

    addOutputData() {
        var data = this.state.newOutputData.trim();
        if (data !== "") {
            if (!(this.state.selectedOutputData.includes(data))) {
                this.state.selectedOutputData.push(this.state.newOutputData)
            }
            this.setState({ 
                selectedOutputData: this.state.selectedOutputData,
                newOutputData: ""
            })
        }
    }

    addBusinessObjective() {
        var data = this.state.businessObjective.trim();
        if (data !== "") {
            if (!(this.state.selectedBusinessObjective.includes(data))) {
                this.state.selectedBusinessObjective.push(this.state.businessObjective)
            }
            this.setState({
                selectedBusinessObjective: this.state.selectedBusinessObjective,
                "businessObjective": ""
            })
        }
    }

    removeInputData() {
        for (var item of this.state.inputDataToBeDeleted) {
            var index = this.state.selectedInputData.indexOf(item);
            if (index !== -1) {
                this.state.selectedInputData.splice(index, 1);
            }
        }
        this.setState({ selectedInputData: this.state.selectedInputData })
    }

    removeOutputData() {
        for (var item of this.state.outputDataToBeDeleted) {
            var index = this.state.selectedOutputData.indexOf(item);
            if (index !== -1) {
                this.state.selectedOutputData.splice(index, 1);
            }
        }
        this.setState({ selectedOutputData: this.state.selectedOutputData })
    }

    removeBusinessObjective() {
        for (var item of this.state.businessObjectiveToBeDeleted) {
            var index = this.state.selectedBusinessObjective.indexOf(item);
            if (index !== -1) {
                this.state.selectedBusinessObjective.splice(index, 1);
            }
        }
        this.setState({ selectedBusinessObjective: this.state.selectedBusinessObjective })
    }


    similarityCheckTresholdHandler = (event) => {
        var value = event.target.value;
        if (value > 1) {
            value = 1;
        }
        if (value < -1) {
            value = -1;
        }
        this.setState({ similarityCheckTreshold: value });
    }

    matching() {
        //ToDo: forward business objectives
        //Set business objectives
        //Test
        //Add result #/total results
        var userData = {
            "businessObjectives": this.state.selectedBusinessObjective,
            "userInputData": this.state.selectedInputData,
            "userOutputData": this.state.selectedOutputData
        }

        GraphService.matching(userData, this.state.similarityCheckTreshold).then(res => {
            //console.log(res)
            var resultsInter = []
            resultsInter.push({"key": "Set of outputs", "value": res.data.semanticSimilarOutputData})   
            resultsInter.push({"key": "Set of inputs", "value": res.data.semanticSimilarInputData})
            resultsInter.push({"key": "Minimal cut sets", "value": res.data.mcs}) 
            resultsInter.push({"key": "Minimal cut sets maching user input and objectives", "value": res.data.mcsmatchingInputsObjective})
            resultsInter.push({"key": "Number of business knowledge found", "value": res.data.reactGraphs.length})                          
            if (res.data.reactGraphs.length > 0) {
                this.setState({
                    graph: res.data.reactGraphs[0],
                    listOfGraphs: res.data.reactGraphs,
                    result: resultsInter,
                    iterator: (this.state.idx+1).toString() + " / " + res.data.reactGraphs.length.toString()
                });
            } else {
                this.setState({
                    graph: {
                        "nodes": [
                        ],
                        "edges": [
                        ]
                    },
                    iterator: ""
                });
            }
            console.log(this.state.listOfGraphs);
            this.showGraph(this.state.graph)
        });

        //console.log(this.state.sessionId)
    }

    reset() {        
        GraphService.getInitialGraph().then(res => {
            this.setState({
                graph: res.data,
                result: [],
                listOfGraphs: [],
                iterator: ""
            });
            this.showGraph(this.state.graph)
        });
    }

    next() {
        if (this.state.listOfGraphs.length > 0) {
            var idx = this.state.idx;
            idx = idx + 1;
            if (idx > this.state.listOfGraphs.length - 1) {
                idx = this.state.listOfGraphs.length - 1;
            }
            this.setState({
                idx: idx,
                iterator: (this.state.idx+1).toString() + " / " + this.state.listOfGraphs.length.toString()
            });
            this.setState({
                graph: this.state.listOfGraphs[idx]
            });
            this.showGraph(this.state.graph);
            console.log(idx);
        }
    }

    previous() {
        if (this.state.listOfGraphs.length > 0) {
            var idx = this.state.idx;
            idx = idx - 1;
            if (idx < 0) {
                idx = 0;
            }
            this.setState({
                idx: idx,
                iterator: (this.state.idx+1).toString() + " / " + this.state.listOfGraphs.length.toString()
            });
            this.setState({
                graph: this.state.listOfGraphs[idx]
            });
            this.showGraph(this.state.graph);
            console.log(idx);
        }
    }

    render() {
        return (
            <div>
                
            <div className="row">
                <table className="table table-striped table-bordered">
                    <tbody>
                        <tr>
                            <td width="20%">Select/Enter and add your input data:</td>
                            <td width="50%">
                                <input type="text" list="inputDataList" placeholder="Enter your input data" className="form-control" value={this.state.newInputData} onChange={this.changeNewInputDataHandler} />
                                <datalist id='inputDataList'>
                                    {
                                        this.state.allInputData.map(
                                            inputData =>
                                                <option key={inputData.name}>{inputData.name}</option>
                                        )
                                    }
                                </datalist><br/>
                                <select multiple={true} className="form-control" onChange={this.changeInputDataSelectHandler}>
                                    {
                                        this.state.selectedInputData.map(
                                            inputData =>
                                                <option key={inputData} value={inputData}>{inputData}</option>
                                        )
                                    }
                                </select>
                            </td>
                            <td width="30%">
                                <button style={{ marginLeft: "10px" }} onClick={() => this.addInputData()} className="btn btn-info">Add input data</button> <br/><br/>
                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeInputData()} className="btn btn-info">Remove input data</button>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">Select/Enter and add your output data:</td>
                            <td width="50%">
                                <input type="text" list="outputDataList" placeholder="Enter your output data" className="form-control" value={this.state.newOutputData} onChange={this.changeNewOutputDataHandler} />
                                <datalist id='outputDataList'>
                                    {
                                        this.state.allOutputData.map(
                                            outputData =>
                                                <option key={outputData.name}>{outputData.name}</option>
                                        )
                                    }
                                </datalist><br/>
                                <select multiple={true} className="form-control" onChange={this.changeOutputDataSelectHandler}>
                                    {
                                        this.state.selectedOutputData.map(
                                            outputData =>
                                                <option key={outputData} value={outputData}>{outputData}</option>
                                        )
                                    }
                                </select>
                            </td>
                            <td width="30%">
                                <button style={{ marginLeft: "10px" }} onClick={() => this.addOutputData()} className="btn btn-info">Add output data</button> <br/><br/>
                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeOutputData()} className="btn btn-info">Remove output data</button>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">Similarity check treshold:</td>
                            <td width="50%">
                                <input type="number" min="-1" step="0.1" max="1" placeholder="Enter the similarity check treshold" className="form-control" value={this.state.similarityCheckTreshold} onChange={this.similarityCheckTresholdHandler}></input>
                            </td>
                            <td width="30%">
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">Enter business objective:</td>
                            <td width="50%">
                                <input type="text" placeholder="Enter your business objective" className="form-control" value={this.state.businessObjective} onChange={this.changeBusinessObjectiveHandler} /><br/>
                                <select multiple={true} className="form-control" onChange={this.changeBusinessObjectiveSelectHandler}>
                                    {
                                        this.state.selectedBusinessObjective.map(
                                            businessObjective =>
                                                <option key={businessObjective} value={businessObjective}>{businessObjective}</option>
                                        )
                                    }
                                </select>
                            </td>
                            <td width="30%">
                                <button style={{ marginLeft: "10px" }} onClick={() => this.addBusinessObjective()} className="btn btn-info">Add business objective</button> <br/><br/>
                                <button style={{ marginLeft: "10px" }} onClick={() => this.removeBusinessObjective()} className="btn btn-info">Remove business objective</button>
                            </td>
                        </tr>                         
                        <tr>
                            <td colSpan="3">                            
                            
                            <button style={{ marginLeft: "10px" }} onClick={() => this.matching()} className="btn btn-info">Search business objective</button> <br/><br/>
                        
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
                <button style={{ marginLeft: "10px" }} onClick={() => this.reset()} className="btn btn-info">Reset</button>
                <br />
                <div width="500" height="20" dangerouslySetInnerHTML={
                    { __html:
                        this.state.result.map(
                            resultData =>
                            "<b>" + resultData.key + ":</b> " + resultData.value + "<br/>"
                        )
                    }
                } />
                <br />    
                <button style={{ marginLeft: "10px" }} onClick={() => this.previous()} className="btn btn-info">Previous reusable business knowledge</button>
                {this.state.iterator}
                <button style={{ marginLeft: "10px" }} onClick={() => this.next()} className="btn btn-info">Next reusable business knowledge</button>                            
                <div className="graph" width="100%" height="100%">
                    <svg id="svg-canvas" width="2500" height="1000" />
                </div>
                <br />
            </div>
        )
    }
}

export default Graph