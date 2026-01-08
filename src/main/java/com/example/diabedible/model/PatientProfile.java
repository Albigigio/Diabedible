package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PatientProfile {

     private RiskFactors riskFactors = new RiskFactors();
    private Comorbidities comorbidities = new Comorbidities();
    private List<String> pastPathologies = new ArrayList<>();
    private String clinicalNotes;

    public PatientProfile() { } 

    @JsonCreator
    public PatientProfile(
            @JsonProperty("riskFactors") RiskFactors riskFactors,
            @JsonProperty("comorbidities") Comorbidities comorbidities,
            @JsonProperty("pastPathologies") List<String> pastPathologies,
            @JsonProperty("clinicalNotes") String clinicalNotes
    ) {
        if (riskFactors != null) this.riskFactors = riskFactors;
        if (comorbidities != null) this.comorbidities = comorbidities;
        if (pastPathologies != null) this.pastPathologies = pastPathologies;
        this.clinicalNotes = clinicalNotes;
    }

    public RiskFactors getRiskFactors() { return riskFactors; }
    public void setRiskFactors(RiskFactors riskFactors) { this.riskFactors = riskFactors; }

    public Comorbidities getComorbidities() { return comorbidities; }
    public void setComorbidities(Comorbidities comorbidities) { this.comorbidities = comorbidities; }

    public List<String> getPastPathologies() { return pastPathologies; }
    public void setPastPathologies(List<String> pastPathologies) { this.pastPathologies = pastPathologies; }

    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String clinicalNotes) { this.clinicalNotes = clinicalNotes; }
}


