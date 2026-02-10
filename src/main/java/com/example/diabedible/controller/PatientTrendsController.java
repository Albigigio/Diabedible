package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class PatientTrendsController implements com.example.diabedible.ViewManaged {

    @FXML private ComboBox<String> patientSelector;
    @FXML private Label weeklyLabel;
    @FXML private Label monthlyLabel;

     private ViewManager viewManager;

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

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


    @FXML
    private void onBack() {
        if (viewManager != null) {
            viewManager.switchScene(
                    FXMLPaths.HOME_DOCTOR,
                    "Home doctore",
                    1200,
                    800,
                    true
            );
        }
    }
}
