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
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.utils.FXMLPaths;



public class TherapyAdminController implements com.example.diabedible.ViewManaged{

    @FXML private TextField therapyNameField, medNameField, doseField;
    @FXML private ListView<String> medList;
    @FXML private ComboBox<String> patientSelector;

    private final List<Medication> meds = new ArrayList<>();
    private final TherapyService therapyService = AppInjector.getTherapyService();

    private ViewManager viewManager;

    @FXML
    private void initialize() {
        var dir = AppInjector.getPatientDirectoryServiceStatic();
        var current = com.example.diabedible.utils.AppSession.getCurrentUser();

        if (current != null) {
            // mostra solo i pazienti assegnati al medico
            patientSelector.getItems().setAll(dir.listPatientsForDoctor(current.getUsername()));
        } else {
            // fallback: se non c'è sessione (o login non attivo), mostra tutti i diabetici
            patientSelector.getItems().setAll(dir.listAllDiabeticUsernames());
        }
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

        var doctor = com.example.diabedible.utils.AppSession.getCurrentUser();

        AppInjector.getAuditServiceStatic().log(new com.example.diabedible.model.AuditEvent(
            null,
            null,
            doctor.getUsername(),
            patientId,
            "Prescritta nuova terapia: " + therapyName
        ));



        // Pulisci la schermata
        therapyNameField.clear();
        medList.getItems().clear();
        meds.clear();


    }
    
    private String getSelectedPatientId() {
        return patientSelector.getValue();
    }

    private String getCurrentDoctorId() {
        var current = com.example.diabedible.utils.AppSession.getCurrentUser();
        return current != null ? current.getUsername() : "doctor-unknown";
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setViewManager(ViewManager viewManager) {
    this.viewManager = viewManager;
    }

    @FXML
    private void onBack() {
        viewManager.switchScene(
            FXMLPaths.HOME_DOCTOR,
            "Home Diabetologo",
            1400,
            900,
            true
    );
}

}
