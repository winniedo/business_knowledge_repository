package de.dmn.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "performance_measure")
public class PerformanceMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rating")
    private double rating; //Linear Numeric Scale

    @Column(name = "datetime")
    private String datetime; //Linear Numeric Scale

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ORGANISATIONALUNIT_ID", referencedColumnName = "ID")
    private OrganisationalUnit decisionMaker;

    public PerformanceMeasure() {
    }

    @Override
    public String toString() {
        return decisionMaker.getName() + " [" + datetime + "] >> " + Double.toString(rating);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof PerformanceMeasure) && (toString().equals(o.toString()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrganisationalUnit getDecisionMaker() {
        return decisionMaker;
    }

    public void setDecisionMaker(OrganisationalUnit decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }    
}
