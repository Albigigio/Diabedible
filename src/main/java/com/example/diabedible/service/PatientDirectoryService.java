package com.example.diabedible.service;

import com.example.diabedible.model.PatientProfile;
import com.example.diabedible.model.PatientRecord;
import com.example.diabedible.model.Role;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.utils.DataStore;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PatientDirectoryService {

    private static final String FILE_PATH = "data/patients.json";

    private final UserRepository userRepository;
    private final List<PatientRecord> records;

    public PatientDirectoryService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
        this.records = DataStore.loadListFromFile(FILE_PATH, new TypeReference<List<PatientRecord>>() {});
    }


    public void assignReferenceDoctor(String patientUsername, String doctorUsername) {
        PatientRecord r = getOrCreateRecord(patientUsername);
        r.setDoctorUsername(doctorUsername);
        touch(r, doctorUsername);
        save();
    }

    public Optional<String> getReferenceDoctorOf(String patientUsername) {
        return records.stream()
                .filter(r -> Objects.equals(r.getPatientUsername(), patientUsername))
                .map(PatientRecord::getDoctorUsername)
                .filter(Objects::nonNull)
                .findFirst();
    }

    public List<String> listPatientsForDoctor(String doctorUsername) {
        // ritorna username pazienti assegnati a quel medico
        return records.stream()
                .filter(r -> Objects.equals(r.getDoctorUsername(), doctorUsername))
                .map(PatientRecord::getPatientUsername)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }


    public PatientProfile getProfile(String patientUsername) {
        return getOrCreateRecord(patientUsername).getProfile();
    }

    public void updateProfile(String patientUsername, String doctorUsername, PatientProfile updated) {
        PatientRecord r = getOrCreateRecord(patientUsername);
        r.setProfile(updated != null ? updated : new PatientProfile());
        touch(r, doctorUsername);
        save();
    }


    public void ensureRecordExists(String patientUsername) {
        getOrCreateRecord(patientUsername);
        save();
    }

    public List<String> listAllDiabeticUsernames() {
        return userRepository.findAll().stream()
                .filter(u -> u.role() == Role.DIABETIC)
                .map(UserRepository.StoredUser::username)
                .sorted()
                .toList();
    }

    private PatientRecord getOrCreateRecord(String patientUsername) {
        return records.stream()
                .filter(r -> Objects.equals(r.getPatientUsername(), patientUsername))
                .findFirst()
                .orElseGet(() -> {
                    PatientRecord r = new PatientRecord();
                    r.setPatientUsername(patientUsername);
                    r.setProfile(new PatientProfile());
                    records.add(r);
                    return r;
                });
    }

    private void touch(PatientRecord r, String doctorUsername) {
        r.setLastUpdatedBy(doctorUsername);
        r.setLastUpdatedAt(LocalDateTime.now());
    }

    private void save() {
        DataStore.saveListToFile(records, FILE_PATH);
    }
}

