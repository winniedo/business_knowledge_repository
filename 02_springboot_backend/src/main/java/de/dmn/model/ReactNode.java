package de.dmn.model;

public class ReactNode {
    private String type;
    private String id;
    private String name;
    private String missing;

    public ReactNode(String type, String id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.missing = "";
    }

    @Override
    public String toString() {
        return type + id + "_" + name;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ReactNode) && (toString().equals(o.toString()));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }
}
