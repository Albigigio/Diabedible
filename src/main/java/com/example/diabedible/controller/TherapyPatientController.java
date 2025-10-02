package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.service.TherapyService;
import com.example.diabedible.utils.AppSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class TherapyPatientController {

    @FXML private VBox medicationContainer; // 👈 ancora presente se vuoi usare i CheckBox
    @FXML private ListView<Medication> medicationList; // 👈 nuova lista

    private final TherapyService therapyService = AppInjector.getTherapyService();

    @FXML
    private void initialize() {
        // Recupera il paziente loggato
        String patientId = AppSession.getCurrentUser().getId();
        Therapy therapy = therapyService.getCurrentTherapy(patientId);

        if (therapy != null) {
            // ✅ Popola la nuova ListView
            medicationList.getItems().setAll(therapy.getMedications());

            // Cella personalizzata con pulsante "Assunto"
            medicationList.setCellFactory(list -> new ListCell<>() {
                private final Button takenBtn = new Button("Assunto");
                {
                    takenBtn.setOnAction(e -> {
                        Medication med = getItem();
                        if (med != null) {
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
                        setText(med.getName() + " - " + med.getDose() +
                                (med.isTaken() ? " ✅" : " ❌"));
                        setGraphic(takenBtn);
                    }
                }
            });

            // 👇 Mantieni anche la vecchia logica coi CheckBox se vuoi
            medicationContainer.getChildren().clear();
            for (Medication m : therapy.getMedications()) {
                CheckBox box = new CheckBox(m.getName() + " (" + m.getDose() + ")");
                box.setSelected(m.isTaken());
                box.selectedProperty().addListener((obs, oldV, newV) -> {
                    m.setTaken(newV);
                    therapyService.prescribeTherapy(therapy); // ri-salva stato aggiornato
                });
                medicationContainer.getChildren().add(box);
            }
        }
    }
}
