package com.example.diabedible.model;

import java.time.LocalDate;

public class PhysicalActivityLog {

    private String patientUsername;
    private LocalDate date;

    public PhysicalActivityLog() {}

    public PhysicalActivityLog(String patientUsername, LocalDate date) {
        this.patientUsername = patientUsername;
        this.date = date;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public LocalDate getDate() {
        return date;
    }
}