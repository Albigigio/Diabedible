package com.example.diabedible.repository;

import com.example.diabedible.model.Symptom;
import java.util.*;

public class InMemorySymptomRepository implements SymptomRepository {
    private final Map<String, List<Symptom>> data = new HashMap<>();

    @Override
    public void save(Symptom symptom) {
        data.computeIfAbsent(symptom.getPatientId(), k -> new ArrayList<>()).add(symptom);
    }

    @Override
    public List<Symptom> findByPatient(String patientId) {
        return data.getOrDefault(patientId, Collections.emptyList());
    }
}
