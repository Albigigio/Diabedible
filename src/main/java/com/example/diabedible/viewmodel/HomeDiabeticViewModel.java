package com.example.diabedible.viewmodel;

import com.example.diabedible.model.reading.DailyReadings;
import com.example.diabedible.model.reading.ReadingCollection;
import com.example.diabedible.model.reading.TimeSlot;

import java.time.LocalDate;
import java.util.*;

/**
 * MVVM-like ViewModel for HomeDiabetic view. Encapsulates domain logic
 * for managing blood sugar readings and exposes simple methods for the UI layer.
 */
public class HomeDiabeticViewModel {

    // Time slot labels sourced from enum for localization support
    public static final String TIME_SLOT_MORNING = TimeSlot.MORNING.displayName();
    public static final String TIME_SLOT_AFTERNOON = TimeSlot.AFTERNOON.displayName();

    private static final double MIN_THRESHOLD = 70.0;
    private static final double MAX_THRESHOLD = 180.0;

    private final ReadingCollection readings = new ReadingCollection();
    // Tracks how many times a slot has been modified for a given day
    private final Map<LocalDate, Map<String, Integer>> modificationCountPerSlot = new HashMap<>();

    private boolean persistEnabled = true;

    public enum AddResult {
        ADDED,
        MODIFIED,
        REJECTED_NOT_TODAY,
        REJECTED_SLOT_NULL,
        REJECTED_TOO_MANY_MODS,
        REJECTED_INVALID_NUMBER
    }

    public double getMinThreshold() {
        return MIN_THRESHOLD;
    }

    public double getMaxThreshold() {
        return MAX_THRESHOLD;
    }

    /**
     * Fills the model with 5 days of simple sample data before today.
     */
    public void initSampleData() {

        persistEnabled = false;

        LocalDate start = LocalDate.now().minusDays(6);
        for (int i = 0; i < 5; i++) {
            LocalDate day = start.plusDays(i);
            addOrReplace(day, TIME_SLOT_MORNING, 100.0 + i * 5);
            addOrReplace(day, TIME_SLOT_AFTERNOON, 110.0 + i * 5);
        }

        persistEnabled = true;
    }

    private void addOrReplace(LocalDate date, String slot, double value) {
    readings.addReading(date, slot, value);
    
    if (!persistEnabled) return;
    
    var user = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (user != null) {

        // mappa slot -> Context (semplice)
            var ctx = slot.equals(TIME_SLOT_AFTERNOON)
                    ? com.example.diabedible.model.reading.BloodSugarReading.Context.AFTER_MEAL
                    : com.example.diabedible.model.reading.BloodSugarReading.Context.BEFORE_MEAL;

            var ts = date.atStartOfDay();

            var r = new com.example.diabedible.model.reading.BloodSugarReading(
                    java.util.UUID.randomUUID().toString(),
                    user.getUsername(),
                    ts,
                    value,
                    ctx
            );

        com.example.diabedible.di.AppInjector.getReadingServiceStatic().addReading(r);
    }
    }

    /**
     * Returns available slots for a given date (i.e., not yet present in data for that day).
     */
    public List<String> availableSlotsFor(LocalDate date) {
        DailyReadings daily = readings.asUnmodifiableMap().get(date);
        List<String> result = new ArrayList<>(2);
        if (daily == null || !daily.getBySlotView().containsKey(TIME_SLOT_MORNING)) {
            result.add(TIME_SLOT_MORNING);
        }
        if (daily == null || !daily.getBySlotView().containsKey(TIME_SLOT_AFTERNOON)) {
            result.add(TIME_SLOT_AFTERNOON);
        }
        return result;
    }

    /**
     * Adds or modifies today's reading for the given slot according to rules:
     * - Only today is allowed
     * - Each slot can be modified at most once after initial insert
     */
    public AddResult addOrModifyReading(LocalDate date, String slot, String readingText) {
        if (slot == null) return AddResult.REJECTED_SLOT_NULL;
        LocalDate today = LocalDate.now();
        if (!today.equals(date)) return AddResult.REJECTED_NOT_TODAY;

        final double value;
        try {
            value = Double.parseDouble(readingText);
        } catch (NumberFormatException ex) {
            return AddResult.REJECTED_INVALID_NUMBER;
        }

        DailyReadings daily = readings.getOrCreate(today);
        boolean isModifying = daily.getBySlotView().containsKey(slot);

        modificationCountPerSlot.putIfAbsent(today, new HashMap<>());
        Map<String, Integer> slotMods = modificationCountPerSlot.get(today);
        int count = slotMods.getOrDefault(slot, 0);

        if (isModifying && count >= 1) {
            return AddResult.REJECTED_TOO_MANY_MODS;
        }

        // Save/modify reading
        addOrReplace(today, slot, value);

        if (isModifying) {
            slotMods.put(slot, count + 1);
            return AddResult.MODIFIED;
        }
        return AddResult.ADDED;
    }

    /**
     * Exposes a simple map view for charting: date -> (slot -> value)
     */
    public Map<LocalDate, Map<String, Double>> asMapForChart() {
        Map<LocalDate, Map<String, Double>> result = new LinkedHashMap<>();
        for (Map.Entry<LocalDate, DailyReadings> entry : readings.asUnmodifiableMap().entrySet()) {
            result.put(entry.getKey(), new LinkedHashMap<>(entry.getValue().getBySlotView()));
        }
        return result;
    }
}
