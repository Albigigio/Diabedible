package com.example.diabedible.model.reading;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Aggregates readings for a single day by time slot.
 * Slots are represented as Strings for now.
 */
public final class DailyReadings {

    private final LocalDate date;
    private final Map<String, Double> bySlot = new LinkedHashMap<>();

    public DailyReadings(LocalDate date) {
        this.date = Objects.requireNonNull(date, "date");
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<String, Double> getBySlotView() {
        return Collections.unmodifiableMap(bySlot);
    }

    public boolean hasSlot(String slot) {
        return bySlot.containsKey(slot);
    }

    public Double getValue(String slot) {
        return bySlot.get(slot);
    }

    public void put(String slot, double value) {
        Objects.requireNonNull(slot, "slot");
        bySlot.put(slot, value);
    }
}
