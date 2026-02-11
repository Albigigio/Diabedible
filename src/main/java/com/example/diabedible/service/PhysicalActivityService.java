package com.example.diabedible.service;

import com.example.diabedible.model.PhysicalActivityLog;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.util.List;

public class PhysicalActivityService {

    private static final String FILE_PATH = "data/physical_activity.json";
    private final List<PhysicalActivityLog> logs;

    public PhysicalActivityService() {
        logs = DataStore.loadListFromFile(
                FILE_PATH,
                new TypeReference<List<PhysicalActivityLog>>() {}
        );
    }

    public void registerActivity(String patientUsername, LocalDate date) {
        logs.add(new PhysicalActivityLog(patientUsername, date));
        DataStore.saveListToFile(logs, FILE_PATH);
    }

    public boolean didActivityOnDate(String patientUsername, LocalDate date) {
        return logs.stream()
                .anyMatch(l ->
                        l.getPatientUsername().equals(patientUsername) &&
                        l.getDate().equals(date)
                );
    }

}