package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a real medication intake performed by a patient.
 */
public class MedicationIntake {

    private String id;
    private String patientUsername;
    private String medicationName;
    private String dose;
    private LocalDateTime timestamp;

    public MedicationIntake() {} // Jackson

    @JsonCreator
    public MedicationIntake(
            @JsonProperty("id") String id,
            @JsonProperty("patientUsername") String patientUsername,
            @JsonProperty("medicationName") String medicationName,
            @JsonProperty("dose") String dose,
            @JsonProperty("timestamp") LocalDateTime timestamp
    ) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.patientUsername = Objects.requireNonNull(patientUsername);
        this.medicationName = Objects.requireNonNull(medicationName);
        this.dose = dose;
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public String getId() { 
        return id; 
    }

    public String getPatientUsername() { 
        return patientUsername; 
    }

    public String getMedicationName() { 
        return medicationName; 
    }

    public String getDose() { 
        return dose; 
    }

    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
}
