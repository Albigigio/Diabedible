package com.example.diabedible.service;

import com.example.diabedible.model.reading.AlertSeverity;
import com.example.diabedible.model.reading.BloodSugarReading;
import com.example.diabedible.model.reading.ReadingTrendSummary;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.DoubleStream;

public class ReadingStatsService {

    private final ReadingService readingService;
    private final ReadingClassifier classifier = new ReadingClassifier();

    public ReadingStatsService(ReadingService readingService) {
        this.readingService = Objects.requireNonNull(readingService);
    }

    public ReadingTrendSummary getTrendForPatient(String patientUsername, int days) {
        LocalDate from = LocalDate.now().minusDays(days - 1);

        List<BloodSugarReading> readings = readingService.getReadingsForPatient(patientUsername).stream()
                .filter(r -> !r.getTimestamp().toLocalDate().isBefore(from))
                .toList();

        if (readings.isEmpty()) {
            return new ReadingTrendSummary(days, 0, 0, 0, 0, 0);
        }

        DoubleStream values = readings.stream().mapToDouble(BloodSugarReading::getValue);
        double avg = readings.stream().mapToDouble(BloodSugarReading::getValue).average().orElse(0);
        double min = readings.stream().mapToDouble(BloodSugarReading::getValue).min().orElse(0);
        double max = readings.stream().mapToDouble(BloodSugarReading::getValue).max().orElse(0);

        long okCount = readings.stream()
                .filter(r -> classifier.classify(r) == AlertSeverity.OK)
                .count();

        double okPercent = (okCount * 100.0) / readings.size();

        return new ReadingTrendSummary(days, readings.size(), avg, min, max, okPercent);
    }
}
