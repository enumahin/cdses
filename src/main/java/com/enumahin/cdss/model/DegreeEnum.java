package com.enumahin.cdss.model;

public enum DegreeEnum {
    D1(0d),
    D2(0.25),
    D3(0.5),
    D4(0.75),
    D5(1d);

    public final Double label;

    private DegreeEnum(Double label){
        this.label= label;
    }
}
