package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.reading.BloodSugarReading;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PatientReadingsController {

    @FXML private ListView<String> listView;

    @FXML
    private void initialize() {
        var doctor = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (doctor == null) return;

        var readings = AppInjector.getReadingServiceStatic()
                .getReadingsForDoctor(doctor.getUsername());

        for (BloodSugarReading r : readings) {
            listView.getItems().add(
                    r.getPatientUsername() + " | " +
                    r.getTimestamp() + " | " +
                    r.getValue() + " (" + r.getContext() + ")"
            );
        }
    }
}
