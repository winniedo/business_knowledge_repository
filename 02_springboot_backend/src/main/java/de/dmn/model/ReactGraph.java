package de.dmn.model;

import java.util.ArrayList;
import java.util.List;

public class ReactGraph {
    private List<ReactNode> nodes;
    private List<ReactEdge> edges;

    public ReactGraph() {
        this.nodes = new ArrayList<ReactNode>();
        this.edges = new ArrayList<ReactEdge>();
    }

    public ReactGraph(List<ReactNode> nodes, List<ReactEdge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<ReactNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ReactNode> nodes) {
        this.nodes = nodes;
    }

    public List<ReactEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<ReactEdge> edges) {
        this.edges = edges;
    }

}
