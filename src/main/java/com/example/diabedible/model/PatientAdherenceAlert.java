package com.example.diabedible.model;

import java.time.LocalDate;

public class PatientAdherenceAlert {
    private final String patientUsername;
    private final int consecutiveDays;
    private final NonAdherenceType type;
    private final LocalDate streakFrom; // giorno più vecchio della streak
    private final LocalDate streakTo;   // giorno più recente (di solito oggi)

    public PatientAdherenceAlert(String patientUsername,
                                 int consecutiveDays,
                                 NonAdherenceType type,
                                 LocalDate streakFrom,
                                 LocalDate streakTo) {
        this.patientUsername = patientUsername;
        this.consecutiveDays = consecutiveDays;
        this.type = type;
        this.streakFrom = streakFrom;
        this.streakTo = streakTo;
    }

    public String getPatientUsername() { return patientUsername; }
    public int getConsecutiveDays() { return consecutiveDays; }
    public NonAdherenceType getType() { return type; }
    public LocalDate getStreakFrom() { return streakFrom; }
    public LocalDate getStreakTo() { return streakTo; }

    @Override
    public String toString() {
        String label = (type == NonAdherenceType.NO_LOGS)
                ? "nessuna registrazione"
                : "registrazioni incomplete";
        return patientUsername + " (" + consecutiveDays + " giorni, " + label + ")";
    }
}
