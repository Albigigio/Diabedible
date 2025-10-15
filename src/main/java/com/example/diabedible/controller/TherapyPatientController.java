package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.service.TherapyService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class TherapyPatientController {

    @FXML private ListView<Medication> medicationList;

    private final TherapyService therapyService = AppInjector.getTherapyService();
    private String patientId = "currentUserId"; // ⚠️ sostituisci con ID reale (es. da AppSession)

    @FXML
    public void initialize() {
        Therapy therapy = therapyService.getCurrentTherapy(patientId);
        if (therapy == null) {
            medicationList.getItems().clear();
            medicationList.setPlaceholder(new Label("Nessuna terapia assegnata."));
            return;
        }

        medicationList.getItems().setAll(therapy.getMedications());

        // ✅ Crea una cella personalizzata con pulsante "Assunto"
        medicationList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Medication> call(ListView<Medication> listView) {
                return new ListCell<>() {
                    private final Button takenBtn = new Button("Assunto");

                    {
                        takenBtn.setOnAction(e -> {
                            Medication med = getItem();
                            if (med != null && !med.isTaken()) {
                                therapyService.markMedicationAsTaken(therapy.getId(), med.getId());
                                med.setTaken(true);
                                medicationList.refresh();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Medication med, boolean empty) {
                        super.updateItem(med, empty);
                        if (empty || med == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            String status = med.isTaken() ? "✅" : "❌";
                            setText(med.getName() + " - " + med.getDose() + " " + status);
                            setGraphic(med.isTaken() ? null : takenBtn);
                        }
                    }
                };
            }
        });
    }
}
