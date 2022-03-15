package com.upb;

public class PerformanceIndicator {
    private String name;
    private String unit;
    private String scaleNormalization;

    public PerformanceIndicator(String name, String unit, String scaleNormalization) {
        this.name = name;
        this.unit = unit;
        this.scaleNormalization = scaleNormalization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getScaleNormalization() {
        return scaleNormalization;
    }

    public void setScaleNormalization(String scaleNormalization) {
        this.scaleNormalization = scaleNormalization;
    }

}
