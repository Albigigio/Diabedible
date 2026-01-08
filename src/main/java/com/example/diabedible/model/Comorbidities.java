package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Comorbidities {

    private boolean hypertension;
    private boolean dyslipidemia;
    private boolean cardiovascularDisease;
    private boolean kidneyDisease;
    private String other;

    public Comorbidities() { } // Jackson

    @JsonCreator
    public Comorbidities(
            @JsonProperty("hypertension") boolean hypertension,
            @JsonProperty("dyslipidemia") boolean dyslipidemia,
            @JsonProperty("cardiovascularDisease") boolean cardiovascularDisease,
            @JsonProperty("kidneyDisease") boolean kidneyDisease,
            @JsonProperty("other") String other
    ) {
        this.hypertension = hypertension;
        this.dyslipidemia = dyslipidemia;
        this.cardiovascularDisease = cardiovascularDisease;
        this.kidneyDisease = kidneyDisease;
        this.other = other;
    }

    public boolean isHypertension() { return hypertension; }
    public void setHypertension(boolean hypertension) { this.hypertension = hypertension; }

    public boolean isDyslipidemia() { return dyslipidemia; }
    public void setDyslipidemia(boolean dyslipidemia) { this.dyslipidemia = dyslipidemia; }

    public boolean isCardiovascularDisease() { return cardiovascularDisease; }
    public void setCardiovascularDisease(boolean cardiovascularDisease) { this.cardiovascularDisease = cardiovascularDisease; }

    public boolean isKidneyDisease() { return kidneyDisease; }
    public void setKidneyDisease(boolean kidneyDisease) { this.kidneyDisease = kidneyDisease; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }
    
}
