package de.dmn.model;

public class ReactEdge {
    private ReactNode source;
    private ReactNode destination;
    private String type;

    public ReactEdge(String type, ReactNode source, ReactNode destination) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    @Override
    public String toString() {
        return type + ":" + source.toString() + " >> " + destination.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ReactEdge) && (toString().equals(o.toString()));
    }

    public ReactNode getSource() {
        return source;
    }

    public void setSource(ReactNode source) {
        this.source = source;
    }

    public ReactNode getDestination() {
        return destination;
    }

    public void setDestination(ReactNode destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
