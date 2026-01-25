package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.MedicationIntake;
import com.example.diabedible.service.IntakeService;
import com.example.diabedible.utils.AppSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.UUID;

public class MedicationIntakeController {

    @FXML
    private TextField medNameField;

    @FXML
    private TextField doseField;

    private final IntakeService intakeService = AppInjector.getIntakeServiceStatic(); // Usa AppInjector per iniezione

    private String currentPatientUsername;

    public void initialize() {
        // Imposta l'username dal sessione corrente (es. paziente loggato)
        currentPatientUsername = AppSession.getCurrentUser().getUsername();
    }

    @FXML
    private void onRegister() {
        String medicationName = medNameField.getText().trim();
        String dose = doseField.getText().trim();

        if (medicationName.isEmpty() || dose.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Per favore, compila tutti i campi.");
            return;
        }

        // Crea la nuova assunzione usando il model
        MedicationIntake intake = new MedicationIntake(
            UUID.randomUUID().toString(),
            currentPatientUsername,
            medicationName,
            dose,
            LocalDateTime.now()
        );

        // Salva usando IntakeService
        try {
            intakeService.registerIntake(intake);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Successo", "Assunzione registrata con successo!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile salvare l'assunzione: " + e.getMessage());
        }
    }

    private void clearFields() {
        medNameField.clear();
        doseField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metodo per impostare manualmente l'username (opzionale, se non usi AppSession)
    public void setPatientUsername(String username) {
        this.currentPatientUsername = username;
    }
}
