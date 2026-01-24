package com.example.diabedible.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditEvent {

    private String id;
    private LocalDateTime timestamp;
    private String doctorUsername;
    private String patientUsername;
    private String action;

    public AuditEvent() {} // Jackson

    @JsonCreator
    public AuditEvent(
            @JsonProperty("id") String id,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("doctorUsername") String doctorUsername,
            @JsonProperty("patientUsername") String patientUsername,
            @JsonProperty("action") String action
    ) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.doctorUsername = doctorUsername;
        this.patientUsername = patientUsername;
        this.action = action;
    }

    public String getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDoctorUsername() { return doctorUsername; }
    public String getPatientUsername() { return patientUsername; }
    public String getAction() { return action; }

    @Override
    public String toString() {
        return timestamp + " | " + doctorUsername +
               " → " + patientUsername +
               " | " + action;
    }
}

