package de.dmn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "business_knowledge_model")
public class BusinessKnowledgeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url")
    private String url;

    public BusinessKnowledgeModel() {

    }
    
    public BusinessKnowledgeModel(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        String d = name;
        return d.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof BusinessKnowledgeModel) && (toString().equals(o.toString()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
