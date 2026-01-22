package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class PatientTrendsController {

    @FXML private ComboBox<String> patientSelector;
    @FXML private Label weeklyLabel;
    @FXML private Label monthlyLabel;

    @FXML
    private void initialize() {
        var doctor = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (doctor == null) return;

        var dir = AppInjector.getPatientDirectoryServiceStatic();
        patientSelector.getItems().setAll(dir.listPatientsForDoctor(doctor.getUsername()));

        if (!patientSelector.getItems().isEmpty()) {
            patientSelector.getSelectionModel().selectFirst();
            refresh();
        }
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    private void refresh() {
        String patient = patientSelector.getValue();
        if (patient == null) return;

        var stats = AppInjector.getReadingStatsServiceStatic();

        var weekly = stats.getTrendForPatient(patient, 7);
        var monthly = stats.getTrendForPatient(patient, 30);

        weeklyLabel.setText(weekly.toString());
        monthlyLabel.setText(monthly.toString());
    }
}
