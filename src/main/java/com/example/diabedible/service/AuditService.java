package com.example.diabedible.service;

import com.example.diabedible.model.AuditEvent;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.stream.Collectors;

public class AuditService {

    private static final String FILE_PATH = "data/audit.json";
    private final List<AuditEvent> events;

    public AuditService() {
        this.events = DataStore.loadListFromFile(
                FILE_PATH,
                new TypeReference<List<AuditEvent>>() {}
        );
    }

    public void log(AuditEvent event) {
        events.add(event);
        DataStore.saveListToFile(events, FILE_PATH);
    }

    public List<AuditEvent> getEventsForDoctor(String doctorUsername) {
        return events.stream()
                .filter(e -> e.getDoctorUsername().equals(doctorUsername))
                .toList();
    }

    public List<AuditEvent> getEventsForPatient(String patientUsername) {
        return events.stream()
                .filter(e -> e.getPatientUsername().equals(patientUsername))
                .toList();
    }
}
