package com.example.diabedible.repository;

import com.example.diabedible.model.Symptom;
import java.util.List;

public interface SymptomRepository {
    void save(Symptom symptom);
    List<Symptom> findByPatient(String patientId);
}
