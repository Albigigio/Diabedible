package com.example.diabedible.service;

import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.repository.TherapyRepository;
import java.util.List;

public class TherapyService {
    private final TherapyRepository repo;

    public TherapyService(TherapyRepository repo) {
        this.repo = repo;
    }

    public void prescribeTherapy(Therapy therapy) {
        repo.save(therapy);
    }

    public List<Therapy> getPatientTherapies(String patientId) {
        return repo.findByPatient(patientId);
    }

    public boolean isAllTaken(String therapyId) {
        Therapy t = repo.findById(therapyId);
        return t != null &&
               t.getMedications().stream().allMatch(Medication::isTaken);
    }

    // ✅ Nuovo metodo per segnare un farmaco come assunto
    public void markMedicationAsTaken(String therapyId, String medicationId) {
        Therapy therapy = repo.findById(therapyId);
        if (therapy != null) {
            therapy.getMedications().stream()
                   .filter(m -> m.getId().equals(medicationId))
                   .findFirst()
                   .ifPresent(m -> m.setTaken(true));
            repo.save(therapy);
        }
    }

    // ✅ Nuovo metodo per ottenere la terapia attuale del paziente
    public Therapy getCurrentTherapy(String patientId) {
        List<Therapy> therapies = repo.findByPatient(patientId);
        if (therapies.isEmpty()) return null;
        return therapies.get(therapies.size() - 1); // prende l'ultima
    }
}
