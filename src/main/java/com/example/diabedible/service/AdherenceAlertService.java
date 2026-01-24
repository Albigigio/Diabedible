package com.example.diabedible.service;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Message;
import com.example.diabedible.model.NonAdherenceType;
import com.example.diabedible.model.PatientAdherenceAlert;
import com.example.diabedible.model.Therapy;

import java.time.LocalDate;
import java.util.*;

public class AdherenceAlertService {

    private final IntakeService intakeService;
    private final TherapyService therapyService;
    private final PatientDirectoryService patientDirectoryService;

    public AdherenceAlertService(IntakeService intakeService,
                                 TherapyService therapyService,
                                 PatientDirectoryService patientDirectoryService) {
        this.intakeService = Objects.requireNonNull(intakeService);
        this.therapyService = Objects.requireNonNull(therapyService);
        this.patientDirectoryService = Objects.requireNonNull(patientDirectoryService);
    }

    /** Alert paziente: true se oggi NON è completa l'aderenza (e c'è una terapia prescritta). */
    public boolean shouldAlertPatientToday(String patientUsername) {
        if (!hasAnyTherapy(patientUsername)) return false;
        return !intakeService.isDailyAdherenceComplete(patientUsername, LocalDate.now());
    }

    /**
     * Alert medico: ritorna la lista dei pazienti che hanno >= thresholdDays
     * giorni consecutivi di NON aderenza negli ultimi lookbackDays giorni (incluso oggi).
     */
    public List<String> patientsWithConsecutiveNonAdherence(String doctorUsername,
                                                            int thresholdDays,
                                                            int lookbackDays) {
        List<String> patients = patientDirectoryService.listPatientsForDoctor(doctorUsername);
        List<String> flagged = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (String p : patients) {
            if (!hasAnyTherapy(p)) continue; // se non c'è terapia, non ha senso segnalare

            int streak = 0;
            for (int i = 0; i < lookbackDays; i++) {
                LocalDate d = today.minusDays(i);
                boolean ok = intakeService.isDailyAdherenceComplete(p, d);

                if (!ok) {
                    streak++;
                    if (streak >= thresholdDays) {
                        flagged.add(p);
                        break;
                    }
                } else {
                    streak = 0; // si interrompe la consecutività
                }
            }
        }

        return flagged;
    }

    private boolean hasAnyTherapy(String patientUsername) {
        List<Therapy> therapies = therapyService.getPatientTherapies(patientUsername);
        return therapies != null && !therapies.isEmpty();
    }

    public List<PatientAdherenceAlert> detailedPatientsWithConsecutiveNonAdherence(
        String doctorUsername,
        int thresholdDays,
        int lookbackDays
        ) {
    List<String> patients = patientDirectoryService.listPatientsForDoctor(doctorUsername);
    List<PatientAdherenceAlert> alerts = new ArrayList<>();

    LocalDate today = LocalDate.now();

    for (String p : patients) {
        if (!hasAnyTherapy(p)) continue;

        int streak = 0;
        LocalDate streakTo = null;
        LocalDate streakFrom = null;

        for (int i = 0; i < lookbackDays; i++) {
            LocalDate d = today.minusDays(i);
            boolean ok = intakeService.isDailyAdherenceComplete(p, d);

            if (!ok) {
                if (streak == 0) streakTo = d;     // primo giorno della streak (più recente)
                streak++;
                streakFrom = d;                    // aggiorna: diventa il più vecchio man mano che vai indietro

                if (streak >= thresholdDays) {
                    // Classifica: NO_LOGS vs INCOMPLETE_LOGS nella finestra della streak
                    boolean anyLogInStreak = intakeService.hasAnyIntakeInDateRange(p, streakFrom, streakTo);

                    NonAdherenceType type = anyLogInStreak
                            ? NonAdherenceType.INCOMPLETE_LOGS
                            : NonAdherenceType.NO_LOGS;

                    alerts.add(new PatientAdherenceAlert(p, streak, type, streakFrom, streakTo));
                    break;
                }
            } else {
                // reset streak
                streak = 0;
                streakFrom = null;
                streakTo = null;
            }
        }

        // invio messaggio al paziente
        AppInjector.getMessageServiceStatic().send( new Message(
            null,
            null,
            "system",
            p, // username paziente
        "Promemoria terapia",
        "Risulta una mancata aderenza alla terapia negli ultimi giorni. " +
        "Contatta il tuo medico o verifica la terapia prescritta.",
        false
        )
    );

    }

    // Ordina: prima NO_LOGS, poi INCOMPLETE_LOGS, e a parità più giorni prima
    alerts.sort(Comparator
            .comparing(PatientAdherenceAlert::getType)
            .thenComparing(PatientAdherenceAlert::getConsecutiveDays)
            .reversed()
    );

    return alerts;

    


}
}
