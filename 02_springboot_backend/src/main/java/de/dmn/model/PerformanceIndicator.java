package de.dmn.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Table(name = "performance_indicator")
public class PerformanceIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "combined_rating")
    private double combinedRating; //Linear Numeric Scale

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PERFORMANCEINDICATOR_ID", referencedColumnName = "ID")
    private List<PerformanceMeasure> performanceMeasures;

    public PerformanceIndicator() {
        this.performanceMeasures = new ArrayList<PerformanceMeasure>();
    }

    public PerformanceIndicator(String name, String unit) {
        super();
        this.name = name;
        this.performanceMeasures = new ArrayList<PerformanceMeasure>();
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
        return (o instanceof PerformanceIndicator) && (toString().equals(o.toString()));
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

    public double getCombinedRating() {
        return combinedRating;
    }

    public void setCombinedRating(double combinedRating) {
        this.combinedRating = combinedRating;
    }

    public List<PerformanceMeasure> getPerformanceMeasures() {
        return performanceMeasures;
    }

    public void setPerformanceMeasures(List<PerformanceMeasure> performanceMeasures) {
        this.performanceMeasures = performanceMeasures;
    }

    public void updateCombinedRating() {        
        double intermediateCombinedRating = 0.0;
        for (PerformanceMeasure performanceMeasure2: this.getPerformanceMeasures()) {
            intermediateCombinedRating = intermediateCombinedRating + performanceMeasure2.getRating();
        }
        if (this.getPerformanceMeasures().size() > 0) {
            intermediateCombinedRating = intermediateCombinedRating / this.getPerformanceMeasures().size();
        }
        this.setCombinedRating(intermediateCombinedRating);
    }

    public void addPerformanceMeasures(PerformanceMeasure performanceMeasure) {
        this.performanceMeasures.add(performanceMeasure);
        this.updateCombinedRating();
    }

    public void removePerformanceMeasures(PerformanceMeasure performanceMeasure) {
        this.performanceMeasures.remove(performanceMeasure);
        this.updateCombinedRating();
    }  
}
