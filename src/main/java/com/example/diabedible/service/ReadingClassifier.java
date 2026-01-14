package com.example.diabedible.service;

import com.example.diabedible.model.reading.AlertSeverity;
import com.example.diabedible.model.reading.BloodSugarReading;

public class ReadingClassifier {

    public AlertSeverity classify(BloodSugarReading r) {
        double v = r.getValue();

        // ipoglicemia grave sempre critica
        if (v < 54) return AlertSeverity.CRITICAL;

        if (r.getContext() == BloodSugarReading.Context.BEFORE_MEAL) {
            if (v >= 70 && v <= 130) return AlertSeverity.OK;
            if ((v >= 54 && v < 70) || (v > 130 && v <= 180)) return AlertSeverity.WARNING;
            return AlertSeverity.CRITICAL; // >180
        }

        // AFTER_MEAL
        if (v <= 180) return AlertSeverity.OK;
        if (v <= 250) return AlertSeverity.WARNING;
        return AlertSeverity.CRITICAL;
    }

    public boolean isOutOfRange(BloodSugarReading r) {
        return classify(r) != AlertSeverity.OK;
    }

}
