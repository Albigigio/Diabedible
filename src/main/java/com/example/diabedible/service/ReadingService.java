package com.example.diabedible.service;

import com.example.diabedible.model.reading.BloodSugarReading;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

public class ReadingService {

    private static final String FILE_PATH = "data/readings.json";

    private final PatientDirectoryService patientDirectoryService;
    private final List<BloodSugarReading> readings;

    public ReadingService(PatientDirectoryService patientDirectoryService) {
        this.patientDirectoryService = patientDirectoryService;
        this.readings = DataStore.loadListFromFile(
                FILE_PATH,
                new TypeReference<List<BloodSugarReading>>() {}
        );
    }

    public void addReading(BloodSugarReading reading) {
        readings.add(reading);
        save();
    }

    public List<BloodSugarReading> getReadingsForPatient(String patientUsername) {
        return readings.stream()
                .filter(r -> r.getPatientUsername().equals(patientUsername))
                .sorted(Comparator.comparing(BloodSugarReading::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<BloodSugarReading> getReadingsForDoctor(String doctorUsername) {
        var patients = patientDirectoryService.listPatientsForDoctor(doctorUsername);

        return readings.stream()
                .filter(r -> patients.contains(r.getPatientUsername()))
                .sorted(Comparator.comparing(BloodSugarReading::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    private void save() {
        DataStore.saveListToFile(readings, FILE_PATH);
    }
}
