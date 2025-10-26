package com.example.diabedible.service;

import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.repository.TherapyRepository;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;

public class TherapyService {
    private static final String FILE_PATH = "data/therapies.json";
    private final List<Therapy> therapies;
    private final ObjectProperty<Therapy> lastUpdatedTherapy = new SimpleObjectProperty<>();

    private final TherapyRepository repo;

    public TherapyService(TherapyRepository repo) {
        this.repo = repo;
        this.therapies = DataStore.loadListFromFile(FILE_PATH, new TypeReference<List<Therapy>>() {});
    }

    public void prescribeTherapy(Therapy therapy) {
        therapies.add(therapy);
        save();
        lastUpdatedTherapy.set(therapy);
    }

    public List<Therapy> getPatientTherapies(String patientId) {
        return therapies.stream()
                .filter(t -> t.getPatientId().equals(patientId))
                .toList();
    }

    public Therapy findById(String therapyId) {
        return therapies.stream()
                .filter(t -> t.getId().equals(therapyId))
                .findFirst()
                .orElse(null);
    }

    public void markMedicationAsTaken(String therapyId, String medicationId) {
        Therapy therapy = findById(therapyId);
        if (therapy != null) {
            therapy.getMedications().stream()
                    .filter(m -> m.getId().equals(medicationId))
                    .findFirst()
                    .ifPresent(m -> m.setTaken(true));
            save();
            lastUpdatedTherapy.set(therapy);
        }
    }

    private void save() {
        DataStore.saveListToFile(therapies, FILE_PATH);
    }

    public ObjectProperty<Therapy> lastUpdatedTherapyProperty() {
        return lastUpdatedTherapy;
    }

    public Therapy getCurrentTherapy(String patientId) {
    List<Therapy> therapies = getPatientTherapies(patientId);
    if (therapies == null || therapies.isEmpty()) return null;
    // se non hai una data in Therapy, prendi l’ultima inserita
    return therapies.get(therapies.size() - 1);
}

}
