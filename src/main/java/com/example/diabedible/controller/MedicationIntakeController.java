package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.MedicationIntake;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class MedicationIntakeController {

    @FXML private TextField medNameField;
    @FXML private TextField doseField;

    @FXML
    private void onRegister() {
        var user = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (user == null) return;

        MedicationIntake intake = new MedicationIntake(
                null,
                user.getUsername(),
                medNameField.getText(),
                doseField.getText(),
                LocalDateTime.now()
        );

        AppInjector.getIntakeServiceStatic().registerIntake(intake);

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText("Assunzione registrata ✅");
        a.showAndWait();

        medNameField.clear();
        doseField.clear();
    }
}

