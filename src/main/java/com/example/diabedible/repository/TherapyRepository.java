package com.example.diabedible.repository;

import com.example.diabedible.model.Therapy;
import java.util.List;

public interface TherapyRepository {
    void save(Therapy therapy);
    List<Therapy> findByPatient(String patientId);

    Therapy findById(String therapyId);
}
