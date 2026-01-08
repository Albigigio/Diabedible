package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RiskFactors {

    private boolean smoker;
    private boolean obese;
    private boolean alcoholUse;
    private boolean substanceAbuse;
    private String notes;

    public RiskFactors() { } // Jackson

    @JsonCreator
    public RiskFactors(
            @JsonProperty("smoker") boolean smoker,
            @JsonProperty("obese") boolean obese,
            @JsonProperty("alcoholUse") boolean alcoholUse,
            @JsonProperty("substanceAbuse") boolean substanceAbuse,
            @JsonProperty("notes") String notes
    ) {
        this.smoker = smoker;
        this.obese = obese;
        this.alcoholUse = alcoholUse;
        this.substanceAbuse = substanceAbuse;
        this.notes = notes;
    }

    public boolean isSmoker() { return smoker; }
    public void setSmoker(boolean smoker) { this.smoker = smoker; }

    public boolean isObese() { return obese; }
    public void setObese(boolean obese) { this.obese = obese; }

    public boolean isAlcoholUse() { return alcoholUse; }
    public void setAlcoholUse(boolean alcoholUse) { this.alcoholUse = alcoholUse; }

    public boolean isSubstanceAbuse() { return substanceAbuse; }
    public void setSubstanceAbuse(boolean substanceAbuse) { this.substanceAbuse = substanceAbuse; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
