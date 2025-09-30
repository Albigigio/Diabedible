package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.utils.AlertUtils;
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Medication;
import com.example.diabedible.model.Therapy;
import com.example.diabedible.service.TherapyService;
import javafx.fxml.FXML;
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

    // ✅ Nuova label per lo stato della terapia
    @FXML private Label statusLabel;

    private ViewManager viewManager;

    // Service per recuperare le terapie reali
    private final TherapyService therapyService = AppInjector.getTherapyService();

    // Simulazione di pazienti e dati glicemici
    private final Map<String, Map<LocalDate, Map<String, Double>>> patientsData = new LinkedHashMap<>();

    private final XYChart.Series<String, Number> morningSeries = new XYChart.Series<>();
    private final XYChart.Series<String, Number> afternoonSeries = new XYChart.Series<>();

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    public void initialize() {
        welcomeText.setText(WELCOME_TEXT);

        // Dummy patient data
        loadDummyData();

        // Popola ComboBox con i pazienti
        patientSelector.getItems().addAll(patientsData.keySet());
        patientSelector.setOnAction(e -> loadPatientData(patientSelector.getValue()));
    }

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

    private void loadPatientData(String patientName) {
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

        // Popola checklist fittizia
        checklistContainer.getChildren().clear();
        for (String task : CHECKLIST_ITEMS) {
            CheckBox checkBox = new CheckBox(task);
            checkBox.setDisable(true);
            checklistContainer.getChildren().add(checkBox);
        }

        // ✅ Controllo stato terapia reale
        List<Therapy> therapies = therapyService.getPatientTherapies(patientName);
        boolean completed = !therapies.isEmpty() &&
                            therapies.stream()
                                     .allMatch(t -> t.getMedications()
                                                     .stream()
                                                     .allMatch(Medication::isTaken));
        if (completed) {
            statusLabel.setText("Terapia completata");
        } else {
            statusLabel.setText("Terapia in corso");
        }
    }

    @FXML
    private void handleLogout() {
        if (viewManager != null) {
            viewManager.getLogoutService().logout(viewManager);
        } else {
            LOGGER.warn("ViewManager non impostato durante il logout");
            showAlert(ALERT_LOGOUT_ERROR);
        }
    }

    private void showAlert(String message) {
        AlertUtils.warning(ALERT_TITLE_WARNING, null, message);
    }
}
