package com.example.diabedible.controller;

import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Symptom;
import com.example.diabedible.service.SymptomService;
import com.example.diabedible.utils.AppSession;
import com.example.diabedible.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.utils.FXMLPaths;

import java.util.List;

public class SymptomController implements com.example.diabedible.ViewManaged{

    @FXML private TextArea descriptionField;
    @FXML private ListView<String> symptomList;

    private final SymptomService symptomService = AppInjector.getSymptomService();

    private ViewManager viewManager;

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    private void initialize() {
        // All’avvio puoi già caricare eventuali sintomi salvati in memoria
        refreshList();
    }

    @FXML
    private void onSaveClicked() {
        String description = descriptionField.getText();
        if (description.isBlank()) return;

        // Usa il nome visualizzato dell'utente come ID paziente per coerenza con le viste del dottore
        User current = AppSession.getCurrentUser();
        String patientId = current != null ? current.getDisplayName() : "Unknown";

        symptomService.addSymptom(patientId, description);
        descriptionField.clear();

        refreshList();
    }

    private void refreshList() {
        User current = AppSession.getCurrentUser();
        String patientId = current != null ? current.getDisplayName() : "Unknown";
        List<Symptom> all = symptomService.listPatientSymptoms(patientId);
        symptomList.getItems().setAll(
                all.stream()
                   .map(s -> s.getDateTime() + " - " + s.getDescription())
                   .toList()
        );
    }

    @FXML
    private void onBack() {
        viewManager.switchScene(
            FXMLPaths.HOME_DIABETIC,
            "Home Diabetico",
            1400,
            900,
            true
    );

    ((javafx.stage.Stage) descriptionField.getScene().getWindow()).close();
    
}


}
