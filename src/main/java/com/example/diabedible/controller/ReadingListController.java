package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.reading.BloodSugarReading;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ReadingListController {

    @FXML private ListView<String> listView;

    @FXML
    private void initialize() {
        var user = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (user == null) return;

        var readings = AppInjector.getReadingServiceStatic()
                .getReadingsForPatient(user.getUsername());

        for (BloodSugarReading r : readings) {
            listView.getItems().add(
                    r.getTimestamp() + " - " +
                    r.getValue() + " (" + r.getContext() + ")"
            );
        }
    }
}
