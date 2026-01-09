package com.example.diabedible.model.reading;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class BloodSugarReading {

    public enum Context {
        BEFORE_MEAL,
        AFTER_MEAL
    }

    private String id;
    private String patientUsername;
    private LocalDateTime timestamp;
    private double value;
    private Context context;

    public BloodSugarReading() {}

    @JsonCreator
    public BloodSugarReading(
            @JsonProperty("id") String id,
            @JsonProperty("patientUsername") String patientUsername,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("value") double value,
            @JsonProperty("context") Context context
    ) {
        this.id = id;
        this.patientUsername = patientUsername;
        this.timestamp = timestamp;
        this.value = value;
        this.context = context;
    }

    public String getId() { 
        return id; 
    }

    public String getPatientUsername() { return patientUsername; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getValue() { return value; }
    public Context getContext() { return context; }
    
    public LocalDate getDate() {
        return timestamp.toLocalDate();
    }

    public String getSlot() {
        int hour = timestamp.getHour();
        if (hour < 12) {
            return "morning";
        } else if (hour < 18) {
            return "afternoon";
        } else {
            return "evening";        
        }    
    }

    
}



