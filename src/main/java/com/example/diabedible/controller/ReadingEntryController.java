package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.reading.BloodSugarReading;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReadingEntryController {

    @FXML private TextField valueField;
    @FXML private ComboBox<BloodSugarReading.Context> contextBox;

    @FXML
    private void initialize() {
        contextBox.getItems().setAll(BloodSugarReading.Context.values());
        contextBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void onSave() {
        double value;
        try {
            value = Double.parseDouble(valueField.getText());
        } catch (NumberFormatException e) {
            showAlert("Valore non valido");
            return;
        }

        var user = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (user == null) return;

        BloodSugarReading r = new BloodSugarReading(
                UUID.randomUUID().toString(),
                user.getUsername(),
                LocalDateTime.now(),
                value,
                contextBox.getValue()
        );

        AppInjector.getReadingServiceStatic().addReading(r);
        showAlert("Glicemia salvata");
        valueField.clear();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
