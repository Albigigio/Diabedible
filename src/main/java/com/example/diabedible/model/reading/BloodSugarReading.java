package com.example.diabedible.model.reading;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain model representing a single blood sugar reading for a specific date and time slot.
 * Note: time slot is represented as a String for now (e.g., "Mattina", "Pomeriggio").
 * A dedicated TimeSlot enum may be introduced in a subsequent task.
 */
public final class BloodSugarReading {

    private final LocalDate date;
    private final String slot;
    private final double value;

    public BloodSugarReading(LocalDate date, String slot, double value) {
        this.date = Objects.requireNonNull(date, "date");
        this.slot = Objects.requireNonNull(slot, "slot");
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSlot() {
        return slot;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BloodSugarReading that)) return false;
        return Double.compare(that.value, value) == 0
                && date.equals(that.date)
                && slot.equals(that.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, slot, value);
    }

    @Override
    public String toString() {
        return "BloodSugarReading{" +
                "date=" + date +
                ", slot='" + slot + '\'' +
                ", value=" + value +
                '}';
    }
}
