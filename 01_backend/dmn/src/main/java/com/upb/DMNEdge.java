package com.upb;

import org.jgrapht.graph.DefaultEdge;

public class DMNEdge extends DefaultEdge {
    private DMNEdgeType edgeType;

    public DMNEdge() {

    }

    public DMNEdge(DMNEdgeType edgeType) {
        this.edgeType = edgeType;
    }

    public DMNEdgeType getEdgeType() {
        return edgeType;
    }

    @Override
    public String toString() {
        return "(" + getSource() + " >> " + getTarget() + " xx " + edgeType + ")";
    }
}
