package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.service.TherapyService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TherapyAdminController {

    @FXML private TextField therapyNameField, medNameField, doseField;
    @FXML private ListView<String> medList;

    // ✅ nuova ComboBox per scegliere il paziente
    @FXML private ComboBox<String> patientSelector;

    private final List<Medication> meds = new ArrayList<>();
    private final TherapyService therapyService = AppInjector.getTherapyService();

    @FXML
    private void initialize() {
        // Popola la combo dei pazienti. Per ora è un esempio statico.
        // Sostituisci con dati reali se hai un repository utenti.
        patientSelector.getItems().addAll("Mario Rossi", "Luisa Bianchi");
    }

    @FXML
    private void onAddMedication() {
        String name = medNameField.getText();
        String dose = doseField.getText();
        if (name.isBlank()) return;
        meds.add(new Medication(UUID.randomUUID().toString(), name, dose));
        medList.getItems().add(name + " - " + dose);
        medNameField.clear();
        doseField.clear();
    }

    @FXML
    private void onPrescribe() {
        String therapyName = therapyNameField.getText();
        if (therapyName.isBlank()) {
            showAlert("Inserisci un nome per la terapia");
            return;
        }

        String patientId = getSelectedPatientId();
        if (patientId == null) {
            showAlert("Seleziona un paziente");
            return;
        }

        String doctorId = getCurrentDoctorId();

        Therapy t = new Therapy(UUID.randomUUID().toString(), patientId, doctorId, therapyName);
        t.getMedications().addAll(meds);
        therapyService.prescribeTherapy(t);

        showAlert("Terapia assegnata con successo a " + patientId);
        // Pulisci la schermata
        therapyNameField.clear();
        medList.getItems().clear();
        meds.clear();
    }

    /** ✅ Ritorna l'ID del paziente selezionato (qui il nome funge da ID) */
    private String getSelectedPatientId() {
        return patientSelector.getValue();
    }

    /** ✅ Ritorna l'ID del medico loggato (demo, da sostituire) */
    private String getCurrentDoctorId() {
        // Se hai un sistema di login, recupera l'ID del medico corrente
        return "doctor-demo";
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
