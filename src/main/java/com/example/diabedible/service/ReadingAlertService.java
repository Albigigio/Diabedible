package com.example.diabedible.service;

import com.example.diabedible.model.reading.AlertSeverity;
import com.example.diabedible.model.reading.BloodSugarReading;
import com.example.diabedible.model.reading.PatientReadingAlert;

import java.util.*;
import java.util.stream.Collectors;

public class ReadingAlertService {

    private final ReadingService readingService;
    private final ReadingClassifier classifier = new ReadingClassifier();

    public ReadingAlertService(ReadingService readingService) {
        this.readingService = Objects.requireNonNull(readingService);
    }

    /**
     * Ritorna, per ogni paziente del medico, l'ULTIMA lettura fuori soglia (se esiste),
     * ordinata per gravità (CRITICAL prima) e recenza.
     */
    public List<PatientReadingAlert> getLatestOutOfRangeAlertsForDoctor(String doctorUsername) {
        List<BloodSugarReading> readings = readingService.getReadingsForDoctor(doctorUsername);

        Map<String, BloodSugarReading> latestByPatient = new HashMap<>();

        // readings sono già ordinati per timestamp desc se il tuo ReadingService lo fa
        // ma qui rendiamo robusto:
        readings.stream()
                .sorted(Comparator.comparing(BloodSugarReading::getTimestamp).reversed())
                .forEach(r -> {
                    if (classifier.classify(r) == AlertSeverity.OK) return;
                    latestByPatient.putIfAbsent(r.getPatientUsername(), r);
                });

        return latestByPatient.values().stream()
                .map(r -> new PatientReadingAlert(
                        r.getPatientUsername(),
                        classifier.classify(r),
                        r.getValue(),
                        r.getContext(),
                        r.getTimestamp()
                ))
                .sorted(Comparator
                        .comparing(PatientReadingAlert::getSeverity).reversed()
                        .thenComparing(PatientReadingAlert::getTimestamp).reversed()
                )
                .collect(Collectors.toList());
    }
}
