package com.example.diabedible.model;

import java.util.ArrayList;
import java.util.List;

public class Therapy {
    private final String id;          // UUID
    private final String patientId;   // Paziente destinatario
    private final String doctorId;    // Medico che la prescrive
    private final String name;        // Nome terapia (es. "Terapia insulinica")
    private final List<Medication> medications = new ArrayList<>();

    public Therapy(String id, String patientId, String doctorId, String name) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.name = name;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getName() { return name; }
    public List<Medication> getMedications() { return medications; }
}
