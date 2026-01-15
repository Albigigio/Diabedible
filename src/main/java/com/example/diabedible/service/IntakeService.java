package com.example.diabedible.service;

import com.example.diabedible.model.Medication;
import com.example.diabedible.model.MedicationIntake;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class IntakeService {

    private static final String FILE_PATH = "data/intakes.json";

    private final TherapyService therapyService;
    private final List<MedicationIntake> intakes;

    public IntakeService(TherapyService therapyService) {
        this.therapyService = therapyService;
        this.intakes = DataStore.loadListFromFile(
                FILE_PATH,
                new TypeReference<List<MedicationIntake>>() {}
        );
    }

    /* REGISTRA ASSUNZIONE */
    public void registerIntake(MedicationIntake intake) {
        intakes.add(intake);
        save();
    }

    /*  QUERY BASE */
    public List<MedicationIntake> getIntakesForPatientOnDate(
            String patientUsername, LocalDate date
    ) {
        return intakes.stream()
                .filter(i -> i.getPatientUsername().equals(patientUsername))
                .filter(i -> i.getTimestamp().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    /* ADERENZA GIORNALIERA */
    public boolean isDailyAdherenceComplete(String patientUsername, LocalDate date) {

        List<Therapy> therapies = therapyService.getPatientTherapies(patientUsername);
        if (therapies.isEmpty()) return true; // nulla prescritto

        Set<String> prescribedMeds = therapies.stream()
                .flatMap(t -> t.getMedications().stream())
                .map(Medication::getName)
                .collect(Collectors.toSet());

        Set<String> takenMeds = getIntakesForPatientOnDate(patientUsername, date)
                .stream()
                .map(MedicationIntake::getMedicationName)
                .collect(Collectors.toSet());

        return takenMeds.containsAll(prescribedMeds);
    }

    private void save() {
        DataStore.saveListToFile(intakes, FILE_PATH);
    }
}
