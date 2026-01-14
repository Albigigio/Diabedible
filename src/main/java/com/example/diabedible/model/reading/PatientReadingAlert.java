package com.example.diabedible.model.reading;

import java.time.LocalDateTime;

public class PatientReadingAlert {
    private final String patientUsername;
    private final AlertSeverity severity;
    private final double value;
    private final BloodSugarReading.Context context;
    private final LocalDateTime timestamp;

    public PatientReadingAlert(String patientUsername,
                               AlertSeverity severity,
                               double value,
                               BloodSugarReading.Context context,
                               LocalDateTime timestamp) {
        this.patientUsername = patientUsername;
        this.severity = severity;
        this.value = value;
        this.context = context;
        this.timestamp = timestamp;
    }

    public String getPatientUsername() { return patientUsername; }
    public AlertSeverity getSeverity() { return severity; }
    public double getValue() { return value; }
    public BloodSugarReading.Context getContext() { return context; }
    public LocalDateTime getTimestamp() { return timestamp; }

   
    public String toString() {
        return patientUsername + " | " + severity + " | " + value + " | " + context + " | " + timestamp;
    }
}
