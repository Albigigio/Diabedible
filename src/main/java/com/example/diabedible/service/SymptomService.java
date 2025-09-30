package com.example.diabedible.service;


import com.example.diabedible.model.Symptom;
import com.example.diabedible.repository.SymptomRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SymptomService {
    private final SymptomRepository repo;

    public SymptomService(SymptomRepository repo) {
        this.repo = repo;
    }

    public void addSymptom(String patientId, String description) {
        Symptom s = new Symptom(UUID.randomUUID().toString(),
                                patientId,
                                description,
                                LocalDateTime.now());
        repo.save(s);
    }

    public List<Symptom> listPatientSymptoms(String patientId) {
        return repo.findByPatient(patientId);
    }
}

