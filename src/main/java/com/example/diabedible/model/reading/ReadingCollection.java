package com.example.diabedible.model.reading;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public final class ReadingCollection {

    private final Map<LocalDate, DailyReadings> byDate = new LinkedHashMap<>();

    public Map<LocalDate, DailyReadings> asUnmodifiableMap() {
        return Collections.unmodifiableMap(byDate);
    }

    public DailyReadings getOrCreate(LocalDate date) {
        Objects.requireNonNull(date, "date");
        return byDate.computeIfAbsent(date, DailyReadings::new);
    }

    public void addReading(BloodSugarReading reading) {
    Objects.requireNonNull(reading, "reading");
    DailyReadings day = getOrCreate(reading.getDate());  
    day.put(reading.getSlot(), reading.getValue());
}


    public void addReading(LocalDate date, String slot, double value) {
        getOrCreate(date).put(slot, value);
    }
}