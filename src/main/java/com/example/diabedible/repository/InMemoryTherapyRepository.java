package com.example.diabedible.repository;

import com.example.diabedible.model.Therapy;
import com.example.diabedible.repository.TherapyRepository;
import java.util.*;

public class InMemoryTherapyRepository implements TherapyRepository {

    private final Map<String, Therapy> data = new HashMap<>();

    @Override
    public void save(Therapy therapy) {
        data.put(therapy.getId(), therapy);
    }

    @Override
    public List<Therapy> findByPatient(String patientId) {
        return data.values().stream()
                   .filter(t -> t.getPatientId().equals(patientId))
                   .toList();
    }

    public Therapy findByTherapyId(String therapyId) {
        return data.get(therapyId);
    }

    public Therapy findById(String therapyId) {
    return data.get(therapyId);
}
}
