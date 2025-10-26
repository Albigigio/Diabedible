package com.example.diabedible.service;

import com.example.diabedible.model.Symptom;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

public class SymptomService {
    private static final String FILE_PATH = "data/symptoms.json";
    private final List<Symptom> symptoms;

    public SymptomService() {
        this.symptoms = DataStore.loadListFromFile(FILE_PATH, new TypeReference<List<Symptom>>() {});
    }

    public void addSymptom(String patientId, String description) {
        Symptom s = new Symptom(UUID.randomUUID().toString(), patientId, description);
        symptoms.add(s);
        save();
    }

    public List<Symptom> listPatientSymptoms(String patientId) {
        return symptoms.stream()
                .filter(s -> s.getPatientId().equals(patientId))
                .sorted(Comparator.comparing(Symptom::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private void save() {
        DataStore.saveListToFile(symptoms, FILE_PATH);
    }
}
