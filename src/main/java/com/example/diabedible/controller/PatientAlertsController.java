package com.example.diabedible.controller;

import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.di.AppInjector;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PatientAlertsController implements com.example.diabedible.ViewManaged{

    @FXML private ListView<String> alertsList;

       private ViewManager viewManager;

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }


    @FXML
    private void initialize() {
        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    @FXML
    private void onBack() {
        if (viewManager != null) {
            viewManager.switchScene(
                    FXMLPaths.HOME_DOCTOR,
                    "Home doctore",
                    1200,
                    800,
                    true
            );
        }
    }

   

    private void refresh() {
        alertsList.getItems().clear();

        var doctor = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (doctor == null) return;

        var alerts = AppInjector.getReadingAlertServiceStatic()
                .getLatestOutOfRangeAlertsForDoctor(doctor.getUsername());

        if (alerts.isEmpty()) {
            alertsList.getItems().add("Nessuna anomalia rilevata ✅");
            return;
        }

        for (var a : alerts) {
            alertsList.getItems().add(a.toString());
        }
    }
}
