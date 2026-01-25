package com.example.diabedible.controller;

import com.example.diabedible.model.MedicationIntake;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.UUID;

public class MedicationIntakeController {

    @FXML
    private TextField medNameField;

    @FXML
    private TextField doseField;

    private String currentPatientUsername; // Dovrà essere impostato durante l'inizializzazione

    public void initialize() {
        // Inizializzazione del controller
    }

    @FXML
    private void onRegister() {
        String medicationName = medNameField.getText().trim();
        String dose = doseField.getText().trim();

        if (medicationName.isEmpty() || dose.isEmpty()) {
            // TODO: Mostrare un messaggio di errore
            return;
        }

        // Crea una nuova registrazione di assunzione farmaco
        MedicationIntake intake = new MedicationIntake(
            UUID.randomUUID().toString(),
            currentPatientUsername,
            medicationName,
            dose,
            LocalDateTime.now()
        );

        // TODO: Salvare l'assunzione usando un service appropriato

        // Pulisce i campi dopo la registrazione
        clearFields();
    }

    private void clearFields() {
        medNameField.clear();
        doseField.clear();
    }

    public void setPatientUsername(String username) {
        this.currentPatientUsername = username;
    }
}
