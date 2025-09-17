package com.example.diabedible.model.reading;

import java.util.Locale;

/**
 * Represents a time slot in the day for blood sugar readings.
 * Provides localized display names while keeping stable enum constants in code.
 */
public enum TimeSlot {
    MORNING("Mattina", "Morning"),
    AFTERNOON("Pomeriggio", "Afternoon");

    private final String itDisplay;
    private final String enDisplay;

    TimeSlot(String itDisplay, String enDisplay) {
        this.itDisplay = itDisplay;
        this.enDisplay = enDisplay;
    }

    /**
     * Returns a localized display name for this time slot based on the provided locale.
     * Currently supports Italian and English; defaults to English for others.
     */
    public String displayName(Locale locale) {
        if (locale != null) {
            String lang = locale.getLanguage();
            if ("it".equalsIgnoreCase(lang)) {
                return itDisplay;
            }
        }
        return enDisplay;
    }

    /**
     * Convenience to get a display name for the default JVM locale.
     */
    public String displayName() {
        return displayName(Locale.getDefault());
    }
}
