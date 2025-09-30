package com.example.diabedible.service;

import com.example.diabedible.model.Therapy;
import com.example.diabedible.repository.TherapyRepository;
import java.util.List;
import com.example.diabedible.model.Medication;

public class TherapyService {
    private final TherapyRepository repo;
    
    public TherapyService(TherapyRepository repo) { this.repo = repo; }

    public void prescribeTherapy(Therapy therapy) {
        repo.save(therapy);
    }

    public List<Therapy> getPatientTherapies(String patientId) {
        return repo.findByPatient(patientId);
    }

    public boolean isAllTaken(String therapyId) {
        return repo.findByTherapyId(therapyId)
                   .getMedications()
                   .stream()
                   .allMatch(Medication::isTaken);
    }
}
