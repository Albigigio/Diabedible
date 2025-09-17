package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.utils.AlertUtils;
import com.example.diabedible.utils.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomeDiabeticController implements ViewManaged {

    // Constants for labels, thresholds, and time slots
    private static final String WELCOME_TEXT = "Benvenuto, Paziente";
    private static final String TIME_SLOT_MORNING = "Mattina";
    private static final String TIME_SLOT_AFTERNOON = "Pomeriggio";
    private static final String SERIES_MIN_THRESHOLD_LABEL = "Minima attenzione";
    private static final String SERIES_MAX_THRESHOLD_LABEL = "Massima attenzione";

    private static final String ALERT_TITLE_WARNING = "Attenzione";
    private static final String ALERT_ONLY_TODAY = "Puoi inserire rilevazioni solo per oggi.";
    private static final String ALERT_SELECT_SLOT = "Seleziona una fascia oraria (Mattina o Pomeriggio).";
    private static final String ALERT_ALREADY_MODIFIED_PREFIX = "Hai già modificato la fascia oraria: ";
    private static final String ALERT_INVALID_NUMBER = "Inserisci un valore numerico valido.";
    private static final String ALERT_LOGOUT_ERROR = "Errore nel gestore delle viste. Impossibile effettuare il logout.";

    private static final String[] CHECKLIST_ITEMS = {
            "Controlla glicemia",
            "Assumi farmaco",
            "Fai attività fisica"
    };

    // Threshold values
    private static final double MIN_THRESHOLD = 70.0;
    private static final double MAX_THRESHOLD = 180.0;

    @FXML private HBox topBar;
    @FXML private Text welcomeText;
    @FXML private Button notificationBtn;
    @FXML private ImageView notificationIcon;

    @FXML private VBox navigation;
    @FXML private ImageView profileImage;
    @FXML private Text profileName;
    @FXML private Text profileType;

    @FXML private TabPane mainContent;
    @FXML private Tab bloodSugarTab;
    @FXML private LineChart<String, Number> bloodSugarChart;
    @FXML private TextField readingField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private Button logoutBtn;
    @FXML private VBox checklistContainer;

    // ViewManager reference
    private ViewManager viewManager;

    // FXML requires a no-arg constructor
    public HomeDiabeticController() { }

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    // Data structures for blood sugar readings
    private final Map<LocalDate, Map<String, Double>> bloodSugarData = new LinkedHashMap<>();
    private XYChart.Series<String, Number> morningSeries;
    private XYChart.Series<String, Number> afternoonSeries;
    private final Map<LocalDate, Map<String, Integer>> modificationCountPerSlot = new HashMap<>();
    private XYChart.Series<String, Number> minThresholdSeries;
    private XYChart.Series<String, Number> maxThresholdSeries;
    private XYChart.Series<String, Number> averageSeries; // media da rivedere..

    @FXML
    public void initialize() {
        // Initialize components that do not depend on ViewManager
        initializeSampleData();
        setupCharts();

        // Welcome text
        welcomeText.setText(WELCOME_TEXT);

        // Default to today
        datePicker.setValue(LocalDate.now());

        // Allow selection of today only
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || !date.equals(today));
            }
        });
        initializeChecklist();
        // Update available time slots
        updateAvailableTimeSlots();
    }

    private void updateAvailableTimeSlots() {
        timeSlotComboBox.getItems().clear();

        LocalDate today = LocalDate.now();
        Map<String, Double> dailyReadings = bloodSugarData.getOrDefault(today, new HashMap<>());

        if (!dailyReadings.containsKey(TIME_SLOT_MORNING)) {
            timeSlotComboBox.getItems().add(TIME_SLOT_MORNING);
        }
        if (!dailyReadings.containsKey(TIME_SLOT_AFTERNOON)) {
            timeSlotComboBox.getItems().add(TIME_SLOT_AFTERNOON);
        }
    }

    private void initializeSampleData() {
        LocalDate start = LocalDate.now().minusDays(6);
        for (int i = 0; i < 5; i++) {
            Map<String, Double> readings = new LinkedHashMap<>();
            readings.put(TIME_SLOT_MORNING, 100.0 + i * 5);
            readings.put(TIME_SLOT_AFTERNOON, 110.0 + i * 5);
            bloodSugarData.put(start.plusDays(i), readings);
        }
    }

    private void setupCharts() {
        // Reading series
        morningSeries = new XYChart.Series<>();
        morningSeries.setName(TIME_SLOT_MORNING);

        afternoonSeries = new XYChart.Series<>();
        afternoonSeries.setName(TIME_SLOT_AFTERNOON);
        // Threshold series
        minThresholdSeries = new XYChart.Series<>();
        minThresholdSeries.setName(SERIES_MIN_THRESHOLD_LABEL);

        maxThresholdSeries = new XYChart.Series<>();
        maxThresholdSeries.setName(SERIES_MAX_THRESHOLD_LABEL);

        // Populate initial series
        updateChartSeries();

        bloodSugarChart.getData().addAll(morningSeries, afternoonSeries, minThresholdSeries, maxThresholdSeries);
    }

    private void updateChartSeries() {
        morningSeries.getData().clear();
        afternoonSeries.getData().clear();
        minThresholdSeries.getData().clear();
        maxThresholdSeries.getData().clear();

        for (Map.Entry<LocalDate, Map<String, Double>> entry : bloodSugarData.entrySet()) {
            String dateLabel = entry.getKey().toString();
            Map<String, Double> readings = entry.getValue();

            if (readings.containsKey(TIME_SLOT_MORNING)) {
                morningSeries.getData().add(new XYChart.Data<>(dateLabel, readings.get(TIME_SLOT_MORNING)));
            }
            if (readings.containsKey(TIME_SLOT_AFTERNOON)) {
                afternoonSeries.getData().add(new XYChart.Data<>(dateLabel, readings.get(TIME_SLOT_AFTERNOON)));
            }
            minThresholdSeries.getData().add(new XYChart.Data<>(dateLabel, MIN_THRESHOLD));
            maxThresholdSeries.getData().add(new XYChart.Data<>(dateLabel, MAX_THRESHOLD));
        }
    }

    @FXML
    private void initializeChecklist() { // da lavorare
        for (String item : CHECKLIST_ITEMS) {
            CheckBox checkBox = new CheckBox(item);
            checklistContainer.getChildren().add(checkBox);
        }
    }

    @FXML
    private void handleAddReading() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate selectedDate = datePicker.getValue();

            if (!selectedDate.equals(today)) {
                showAlert(ALERT_ONLY_TODAY);
                return;
            }

            String selectedSlot = timeSlotComboBox.getValue();
            if (selectedSlot == null) {
                showAlert(ALERT_SELECT_SLOT);
                return;
            }

            double reading = Double.parseDouble(readingField.getText());

            // Initialize daily map if needed
            bloodSugarData.putIfAbsent(today, new LinkedHashMap<>());
            Map<String, Double> dailyReadings = bloodSugarData.get(today);

            boolean isModifying = dailyReadings.containsKey(selectedSlot);

            // Init modification map for today
            modificationCountPerSlot.putIfAbsent(today, new HashMap<>());
            Map<String, Integer> slotModifications = modificationCountPerSlot.get(today);
            int slotModificationCount = slotModifications.getOrDefault(selectedSlot, 0);

            if (isModifying && slotModificationCount >= 1) {
                showAlert(ALERT_ALREADY_MODIFIED_PREFIX + selectedSlot);
                return;
            }

            // Save/modify reading
            dailyReadings.put(selectedSlot, reading);

            // If it is a modification, increment counter for the slot
            if (isModifying) {
                slotModifications.put(selectedSlot, slotModificationCount + 1);
            }

            updateChartSeries();
            readingField.clear();
            timeSlotComboBox.getSelectionModel().clearSelection();
            updateAvailableTimeSlots();

        } catch (NumberFormatException ex) {
            showAlert(ALERT_INVALID_NUMBER);
        }
    }

    @FXML
    private void handleLogout() {
        if (viewManager != null) {
            viewManager.logout();
        } else {
            showAlert(ALERT_LOGOUT_ERROR);
        }
    }

    private void showAlert(String message) {
        AlertUtils.warning(ALERT_TITLE_WARNING, null, message);
    }
}
