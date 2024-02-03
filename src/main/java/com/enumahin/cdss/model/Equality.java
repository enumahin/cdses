package com.enumahin.cdss.model;

public enum Equality {
    LT("LESS_THAN"),
    LTE("LESS_THAN_OR_EQUAL_TO"),
    GT("GREATER_THAN"),
    GTE("GREATER_THAN_OR_EQUAL_TO"),
    EQ("EQUAL_TO");

    public final String label;

    private Equality(String label){
        this.label= label;
    }
}
