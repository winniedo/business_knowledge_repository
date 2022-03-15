package de.dmn.service.graph;

import org.jgrapht.graph.DefaultEdge;

public class SimpleEdge extends DefaultEdge {
    public SimpleEdge() {

    }

    public String getSource(){
        return (String) super.getSource();
    }

    public String getTarget(){
        return (String) super.getTarget();
    }

    @Override
    public String toString() {
        return "(" + getSource() + " >> " + getTarget() + ")";
    }
}
