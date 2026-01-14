package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PatientAlertsController {

    @FXML private ListView<String> alertsList;

    @FXML
    private void initialize() {
        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    private void refresh() {
        alertsList.getItems().clear();

        var doctor = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (doctor == null) return;

        var alerts = AppInjector.getReadingAlertServiceStatic()
                .getLatestOutOfRangeAlertsForDoctor(doctor.getUsername());

        if (alerts.isEmpty()) {
            alertsList.getItems().add("Nessuna anomalia rilevata ✅");
            return;
        }

        for (var a : alerts) {
            alertsList.getItems().add(a.toString());
        }
    }
}
