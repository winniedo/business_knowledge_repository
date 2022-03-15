import React, { useState } from 'react';
import ReactFlow, { ReactFlowProvider, addEdge, removeElements, Controls, isNode, Position, } from 'react-flow-renderer';
import dagre from 'dagre';
import './layouting.css';

const position = { x: 0, y: 0 };
const edgeType = 'smoothstep';
var initialElements = [
    {
      id: '1',
      type: 'input',
      data: { label: 'input' },
      position,
    },
    {
      id: '2',
      data: { label: 'node 2' },
      position,
    },
    {
      id: '2a',
      data: { label: 'node 2a' },
      position,
    },
    {
      id: '2b',
      data: { label: 'node 2b' },
      position,
    },
    {
      id: '2c',
      data: { label: 'node 2c' },
      position,
    },
    {
      id: '2d',
      data: { label: 'node 2d' },
      position,
    },
    {
      id: '3',
      data: { label: 'node 3' },
      position,
    },
    {
      id: '4',
      data: { label: 'node 4' },
      position,
    },
    {
      id: '5',
      data: { label: 'node 5' },
      position,
    },
    {
      id: '6',
      type: 'output',
      data: { label: 'output' },
      position,
    },
    { id: '7', type: 'output', data: { label: 'output' }, position },
    { id: 'e12', source: '1', target: '2', type: edgeType, animated: true },
    { id: 'e13', source: '1', target: '3', type: edgeType, animated: true },
    { id: 'e22a', source: '2', target: '2a', type: edgeType, animated: true },
    { id: 'e22b', source: '2', target: '2b', type: edgeType, animated: true },
    { id: 'e22c', source: '2', target: '2c', type: edgeType, animated: true },
    { id: 'e2c2d', source: '2c', target: '2d', type: edgeType, animated: true },
    { id: 'e45', source: '4', target: '5', type: edgeType, animated: true },
    { id: 'e56', source: '5', target: '6', type: edgeType, animated: true },
    { id: 'e57', source: '5', target: '7', type: edgeType, animated: true },
  ];

const dagreGraph = new dagre.graphlib.Graph();
dagreGraph.setDefaultEdgeLabel(() => ({}));
const nodeExtent = [
    [0, 0],
    [1000, 1000],
];
const LayoutFlow = () => {
    const [elements, setElements] = useState(initialElements);
    const onConnect = (params) => setElements((els) => addEdge(params, els));
    const onElementsRemove = (elementsToRemove) => setElements((els) => removeElements(elementsToRemove, els));
    const onLayout = (direction) => {
        const isHorizontal = direction === 'LR';
        dagreGraph.setGraph({ rankdir: direction });
        elements.forEach((el) => {
            if (isNode(el)) {
                dagreGraph.setNode(el.id, { width: 150, height: 50 });
            }
            else {
                dagreGraph.setEdge(el.source, el.target);
            }
        });
        dagre.layout(dagreGraph);
        const layoutedElements = elements.map((el) => {
            if (isNode(el)) {
                const nodeWithPosition = dagreGraph.node(el.id);
                el.targetPosition = isHorizontal ? Position.Left : Position.Top;
                el.sourcePosition = isHorizontal ? Position.Right : Position.Bottom;
                // we need to pass a slightly different position in order to notify react flow about the change
                // @TODO how can we change the position handling so that we dont need this hack?
                el.position = { x: nodeWithPosition.x + Math.random() / 1000, y: nodeWithPosition.y };
            }
            return el;
        });
        setElements(layoutedElements);
    };
    return (<div className="layoutflow">
      <ReactFlowProvider>
        <ReactFlow elements={elements} onConnect={onConnect} onElementsRemove={onElementsRemove} nodeExtent={nodeExtent} onLoad={() => onLayout('TB')}>
          <Controls />
        </ReactFlow>
        <div className="controls">
          <button onClick={() => onLayout('TB')} style={{ marginRight: 10 }}>
            vertical layout
          </button>
          <button onClick={() => onLayout('LR')}>horizontal layout</button>
        </div>
      </ReactFlowProvider>
    </div>);
};
export default LayoutFlow;
