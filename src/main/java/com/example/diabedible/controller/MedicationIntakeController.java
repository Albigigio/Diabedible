package com.example.diabedible.controller;
package com.example.diabedible.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MedicationIntakeController {

    @FXML
    private TextField medNameField;

    @FXML
    private TextField doseField;

    @FXML
    private void onRegister(ActionEvent event) {
        String name = medNameField != null ? medNameField.getText() : "";
        String dose = doseField != null ? doseField.getText() : "";

        if (name.isBlank() || dose.isBlank()) {
            Alert warn = new Alert(Alert.AlertType.WARNING);
            warn.setTitle("Dati mancanti");
            warn.setHeaderText(null);
            warn.setContentText("Inserisci nome del farmaco e dose.");
            warn.showAndWait();
            return;
        }

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Assunzione registrata");
        info.setHeaderText(null);
        info.setContentText("Registrata assunzione di " + name + " (" + dose + ").");
        info.showAndWait();

        medNameField.clear();
        doseField.clear();
    }
}
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

