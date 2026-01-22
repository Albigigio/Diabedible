package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.utils.AlertUtils;
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.service.TherapyService;
import com.example.diabedible.service.SymptomService;
import javafx.fxml.FXML;
import com.example.diabedible.utils.FXMLPaths;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeDoctorController implements ViewManaged {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeDoctorController.class);

    // Constants
    private static final String WELCOME_TEXT = "Benvenuto, Dottore";
    private static final String TIME_SLOT_MORNING = "Mattina";
    private static final String TIME_SLOT_AFTERNOON = "Pomeriggio";
    private static final String ALERT_TITLE_WARNING = "Attenzione";
    private static final String ALERT_LOGOUT_ERROR = "Errore: impossibile effettuare il logout.";
    private static final String[] CHECKLIST_ITEMS = {"Controlla glicemia", "Assumi farmaco", "Fai attività fisica"};

    @FXML private HBox topBar;
    @FXML private Text welcomeText;
    @FXML private VBox checklistContainer;
    @FXML private ComboBox<String> patientSelector;
    @FXML private LineChart<String, Number> bloodSugarChart;
    @FXML private Button logoutBtn;
    @FXML private Label statusLabel;
    @FXML private ListView<String> symptomList; // ✅ Lista sintomi

    private ViewManager viewManager;

    // Services
    private final TherapyService therapyService = AppInjector.getTherapyService();
    private final SymptomService symptomService = AppInjector.getSymptomService();

    // Simulazione di pazienti e dati glicemici
    private final Map<String, Map<LocalDate, Map<String, Double>>> patientsData = new LinkedHashMap<>();

    private final XYChart.Series<String, Number> morningSeries = new XYChart.Series<>();
    private final XYChart.Series<String, Number> afternoonSeries = new XYChart.Series<>();

    
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    //Inizializzazione e listener automatico
    
    public void initialize() {
        welcomeText.setText(WELCOME_TEXT);
        loadDummyData();

        // Popola ComboBox con pazienti
        patientSelector.getItems().addAll(patientsData.keySet());
        patientSelector.setOnAction(e -> loadPatientData(patientSelector.getValue()));

        //  Ascolta modifiche dal TherapyService
        AppInjector.getTherapyService().lastUpdatedTherapyProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && patientSelector.getValue() != null &&
                newVal.getPatientId().equals(patientSelector.getValue())) {
                refreshTherapyStatus(newVal);
            }
        });

        var doctor = com.example.diabedible.utils.AppSession.getCurrentUser();
        if (doctor != null) {
            var alerts = AppInjector.getAdherenceAlertServiceStatic().detailedPatientsWithConsecutiveNonAdherence(doctor.getUsername(), 3, 14);

            if (!alerts.isEmpty()) {

                StringBuilder noLogs = new StringBuilder();
                StringBuilder incomplete = new StringBuilder();

                for (var a : alerts) {
                    if (a.getType() == com.example.diabedible.model.NonAdherenceType.NO_LOGS) {
                        noLogs.append("- ").append(a.toString()).append("\n");
                    } else {
                        incomplete.append("- ").append(a.toString()).append("\n");
                    }
                }
                StringBuilder msg = new StringBuilder("Attenzione: non aderenza ≥ 3 giorni consecutivi.\n\n");

                if (!noLogs.isEmpty()) {
                    msg.append("Nessuna registrazione:\n").append(noLogs).append("\n");
                }
        
                if (!incomplete.isEmpty()) {
                    msg.append("Registrazioni incomplete:\n").append(incomplete);
                }

                AlertUtils.warning("Pazienti non aderenti", null, msg.toString());
            }
        }

       
    }

    //  Dati fittizi del grafico
    private void loadDummyData() {
        Map<LocalDate, Map<String, Double>> data = new LinkedHashMap<>();
        LocalDate today = LocalDate.now().minusDays(4);
        for (int i = 0; i < 4; i++) {
            Map<String, Double> readings = new LinkedHashMap<>();
            readings.put(TIME_SLOT_MORNING, 95.0 + i * 4);
            readings.put(TIME_SLOT_AFTERNOON, 105.0 + i * 3);
            data.put(today.plusDays(i), readings);
        }
        patientsData.put("Mario Rossi", data);
        patientsData.put("Luisa Bianchi", data); // stesso dummy
    }

    // Caricamento dati paziente
    private void loadPatientData(String patientName) {
        // ✅ Grafico glicemia
        bloodSugarChart.getData().clear();
        morningSeries.getData().clear();
        afternoonSeries.getData().clear();

        morningSeries.setName(TIME_SLOT_MORNING);
        afternoonSeries.setName(TIME_SLOT_AFTERNOON);

        Map<LocalDate, Map<String, Double>> data = patientsData.get(patientName);
        if (data != null) {
            for (Map.Entry<LocalDate, Map<String, Double>> entry : data.entrySet()) {
                String date = entry.getKey().toString();
                Map<String, Double> values = entry.getValue();
                if (values.containsKey(TIME_SLOT_MORNING)) {
                    morningSeries.getData().add(new XYChart.Data<>(date, values.get(TIME_SLOT_MORNING)));
                }
                if (values.containsKey(TIME_SLOT_AFTERNOON)) {
                    afternoonSeries.getData().add(new XYChart.Data<>(date, values.get(TIME_SLOT_AFTERNOON)));
                }
            }
        }
        bloodSugarChart.getData().addAll(morningSeries, afternoonSeries);

        // Checklist fittizia
        checklistContainer.getChildren().clear();
        for (String task : CHECKLIST_ITEMS) {
            CheckBox checkBox = new CheckBox(task);
            checkBox.setDisable(true);
            checklistContainer.getChildren().add(checkBox);
        }

        // Stato terapia
        List<Therapy> therapies = therapyService.getPatientTherapies(patientName);
        boolean completed = !therapies.isEmpty() &&
                therapies.stream()
                        .allMatch(t -> t.getMedications()
                                .stream()
                                .allMatch(Medication::isTaken));
        statusLabel.setText(completed ? "Terapia completata ✅" : "Terapia in corso ❌");

        // Sintomi del paziente
        symptomList.getItems().clear();
        symptomService.listPatientSymptoms(patientName).forEach(symptom -> {
            symptomList.getItems().add(
                    symptom.getDateTime() + " - " + symptom.getDescription()
            );
        });

        boolean adherenceToday =
            AppInjector.getIntakeServiceStatic().isDailyAdherenceComplete(
                    patientSelector.getValue(),
                    LocalDate.now()
                );

        statusLabel.setText(
            adherenceToday
                ? "Aderenza terapia oggi ✅"
                : "Aderenza terapia oggi ❌"
            );
        
    }

    //  Metodo richiamato automaticamente quando cambia lo stato terapia
    private void refreshTherapyStatus(Therapy therapy) {
        boolean completed = therapy.getMedications().stream().allMatch(Medication::isTaken);
        statusLabel.setText(completed ? "Terapia completata ✅" : "Terapia in corso ❌");

        // (opzionale) notifica visiva
        if (completed) {
            AlertUtils.info("Aggiornamento", 
                            null, 
                            "Il paziente ha completato la terapia ✅"
                        );
        }
    }

    @FXML
    private void handleLogout() {
        if (viewManager != null) {
            viewManager.getLogoutService().logout(viewManager);
        } else {
            LOGGER.warn(
                "ViewManager non impostato durante il logout"
            );
            showAlert(ALERT_LOGOUT_ERROR);
        }
    }

    private void showAlert(String message) {
        AlertUtils.warning(ALERT_TITLE_WARNING, null, message);
    }

    @FXML
    private void onAlerts() {
        if (viewManager != null) {
            viewManager.switchScene(
                FXMLPaths.PATIENT_ALERTS,
                "Segnalazioni glicemia",
                1200,
                800,
                true
            );
        } else {
            LOGGER.warn("ViewManager non impostato durante la navigazione alle segnalazioni");
            AlertUtils.warning("Errore", null, "Impossibile aprire le segnalazioni");
        }
}

    @FXML
    private void onTrends() {
        if (viewManager != null) {
            viewManager.switchScene(
                FXMLPaths.PATIENT_TRENDS,
                "Trend glicemia",
                1200,
                800,
                true
            );
        } else {
            LOGGER.warn("ViewManager non impostato durante la navigazione ai trend");
            AlertUtils.warning("Errore", null, "Impossibile aprire i trend");
        }
    }
    
}