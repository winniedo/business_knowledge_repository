package de.dmn.service.graph;

import org.jgrapht.graph.DefaultEdge;

public class DMNEdge extends DefaultEdge {
    public DMNEdge() {

    }

    public DMNVertex getSource(){
        return (DMNVertex) super.getSource();
    }

    public DMNVertex getTarget(){
        return (DMNVertex) super.getTarget();
    }

    @Override
    public String toString() {
        return "(" + getSource() + " >> " + getTarget() + ")";
    }
}
