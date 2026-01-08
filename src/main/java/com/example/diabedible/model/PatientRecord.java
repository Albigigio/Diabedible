package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PatientRecord {
    private String patientUsername;
    private String doctorUsername; // medico di riferimento
    private PatientProfile profile = new PatientProfile();

    private String lastUpdatedBy;  // username medico
    private LocalDateTime lastUpdatedAt;

    public PatientRecord() { } // Jackson

    @JsonCreator
    public PatientRecord(
            @JsonProperty("patientUsername") String patientUsername,
            @JsonProperty("doctorUsername") String doctorUsername,
            @JsonProperty("profile") PatientProfile profile,
            @JsonProperty("lastUpdatedBy") String lastUpdatedBy,
            @JsonProperty("lastUpdatedAt") LocalDateTime lastUpdatedAt
    ) {
        this.patientUsername = patientUsername;
        this.doctorUsername = doctorUsername;
        if (profile != null) this.profile = profile;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getPatientUsername() { return patientUsername; }
    public void setPatientUsername(String patientUsername) { this.patientUsername = patientUsername; }

    public String getDoctorUsername() { return doctorUsername; }
    public void setDoctorUsername(String doctorUsername) { this.doctorUsername = doctorUsername; }

    public PatientProfile getProfile() { return profile; }
    public void setProfile(PatientProfile profile) { this.profile = profile; }

    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }
}
