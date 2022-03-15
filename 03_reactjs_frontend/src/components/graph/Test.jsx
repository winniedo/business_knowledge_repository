import React, { Component } from 'react';
import ReactDOM from "react-dom";
import * as d3 from "d3";
import dagreD3 from "dagre-d3";

import "./layouting.css";


class Test extends Component {

	constructor(props) {
		super(props);

		this.state = {
			elements: [
				{ id: '1', data: { label: 'Node 1' }, position: { x: 250, y: 5 } },
				{ id: '2', data: { label: 'Node 2' }, position: { x: 100, y: 100 } },
				{ id: '3', data: { label: 'Node 3' }, position: { x: 100, y: 100 } },
				{ id: 'e1-2', source: '1', target: '2', animated: true },
				{ id: 'e1-3', source: '1', target: '2', animated: true },
			],
			flowStyles: { height: 500 }
		}
	}

	componentDidMount() {
		// Create the input graph
		var g = new dagreD3.graphlib.Graph({ compound: true })
			.setGraph({})
			.setDefaultEdgeLabel(function () {
				return {};
			});

		// Here we're setting the nodes
		
		g.setNode("b", { label: "B" });
		g.setNode("c", { label: "C" });
		g.setNode("d", { label: "D" });
		g.setNode("e", { label: "E" });
		g.setNode("f", { label: "F" });
		g.setNode("g", { label: "G" });
		g.setNode("a", { label: "Eligibility", style: "fill: #5f9488", arrowhead: "vee"});

		// Set up edges, no special attributes.
		
		g.setEdge("b", "c", { arrowhead: "normal"});
		g.setEdge("b", "d", { arrowhead: "vee"});
		g.setEdge("b", "e");
		g.setEdge("b", "f");
		g.setEdge("b", "g");
		g.setEdge("a", "b");

		g.nodes().forEach(function (v) {
			var node = g.node(v);
			// Round the corners of the nodes
			node.rx = node.ry = 5;
		});

		// Create the renderer
		var render = new dagreD3.render();

		// Set up an SVG group so that we can translate the final graph.
		var svg = d3.select("svg"),
			svgGroup = svg.append("g");

		// Run the renderer. This is what draws the final graph.
		render(d3.select("svg g"), g);

		// Center the graph
		console.log(svg.attr("width"));
		var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
		console.log(xCenterOffset);
		svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
		svg.attr("height", g.graph().height + 40);
	}

	render() {
		return (
		  <div className="App">
			<h1>Hello CodeSandbox</h1>
			<h2>Start editing to see some magic happen!</h2>
			<svg id="svg-canvas" width="800" height="600" />
		  </div>
		);
	  }
}

export default Test