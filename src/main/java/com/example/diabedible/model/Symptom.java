package com.example.diabedible.model;

import java.time.LocalDateTime;

public class Symptom {
    private final String id;
    private final String patientId;   // ✅ questo campo
    private String description;
    private LocalDateTime dateTime;

    public Symptom(String id, String patientId, String description, LocalDateTime dateTime) {
        this.id = id;
        this.patientId = patientId;
        this.description = description;
        this.dateTime = dateTime;
    }

    // ✅ Aggiungi questo getter
    public String getPatientId() {
        return patientId;
    }

    
    public LocalDateTime getDateTime() { 
    return dateTime; 
    }

    public String getDescription() { 
    return description; 
    }

}
