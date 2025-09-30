package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Symptom;
import com.example.diabedible.service.SymptomService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;

public class SymptomController {

    @FXML private TextArea descriptionField;
    @FXML private ListView<String> symptomList;

    private final SymptomService symptomService =
            AppInjector.getSymptomService();

    private String patientId = "currentUserId"; // recupera l’ID reale del paziente loggato

    @FXML
    private void initialize() {
        // All’avvio puoi già caricare eventuali sintomi salvati in memoria
        refreshList();
    }

    @FXML
    private void onSaveClicked() {
        String description = descriptionField.getText();
        if (description.isBlank()) return;

        symptomService.addSymptom(patientId, description);
        descriptionField.clear();

        refreshList();
    }

    private void refreshList() {
        List<Symptom> all = symptomService.listPatientSymptoms(patientId);
        symptomList.getItems().setAll(
                all.stream()
                   .map(s -> s.getDateTime() + " - " + s.getDescription())
                   .toList()
        );
    }
}
