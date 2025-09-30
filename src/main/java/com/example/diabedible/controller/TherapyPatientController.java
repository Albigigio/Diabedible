package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.service.TherapyService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class TherapyPatientController {

    @FXML private VBox medicationContainer;
    private final TherapyService therapyService = AppInjector.getTherapyService();
    private String patientId = "currentUserId"; // sostituisci con utente loggato

    @FXML
    private void initialize() {
        for (Therapy t : therapyService.getPatientTherapies(patientId)) {
            for (Medication m : t.getMedications()) {
                CheckBox box = new CheckBox(m.getName() + " (" + m.getDose() + ")");
                box.setSelected(m.isTaken());
                box.selectedProperty().addListener((obs, oldV, newV) -> {
                    m.setTaken(newV);
                    therapyService.prescribeTherapy(t); // ri-salva stato aggiornato
                });
                medicationContainer.getChildren().add(box);
            }
        }
    }
}


