package com.example.diabedible.controller;

import com.example.diabedible.Main;
import com.example.diabedible.ViewManaged;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.CheckListView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomeDiabeticController implements ViewManaged {

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

    //viewmanager
    private ViewManager viewManager;

    // Costruttore vuoto richiesto da FXML
    public HomeDiabeticController() {
        // Costruttore vuoto necessario per FXML
    }

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    // variabili per la gestione dei dati di rilevazione glicemica
    private final Map<LocalDate, Map<String, Double>> bloodSugarData = new LinkedHashMap<>();
    private XYChart.Series<String, Number> morningSeries;
    private XYChart.Series<String, Number> afternoonSeries;
    private final Map<LocalDate, Map<String, Integer>> modificationCountPerSlot = new HashMap<>();
    private XYChart.Series<String, Number> minThresholdSeries;
    private XYChart.Series<String, Number> maxThresholdSeries;
    private XYChart.Series<String, Number> averageSeries; // media da rivedere..

    private static final double MIN_THRESHOLD = 70.0;
    private static final double MAX_THRESHOLD = 180.0;

    @FXML
    public void initialize() {
        // Nota: ViewManager sarà inizializzato dopo il caricamento del controller
        // Inizializzare i componenti indipendenti dal ViewManager
        initializeSampleData();
        setupCharts();

        // Testo di benvenuto
        welcomeText.setText("Benvenuto, Paziente");

        // Imposta oggi di default
        datePicker.setValue(LocalDate.now());

        // Permette solo la selezione di oggi
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || !date.equals(today));
            }
        });
        initializeChecklist();
        // update slot di tempo disponibili
        updateAvailableTimeSlots();
    }
    private void updateAvailableTimeSlots() {
        timeSlotComboBox.getItems().clear();

        LocalDate today = LocalDate.now();
        Map<String, Double> dailyReadings = bloodSugarData.getOrDefault(today, new HashMap<>());

        if (!dailyReadings.containsKey("Mattina")) {
            timeSlotComboBox.getItems().add("Mattina");
        }
        if (!dailyReadings.containsKey("Pomeriggio")) {
            timeSlotComboBox.getItems().add("Pomeriggio");
        }
    }

    private void initializeSampleData() {
        LocalDate today = LocalDate.now().minusDays(6);
        for (int i = 0; i < 5; i++) {
            Map<String, Double> readings = new LinkedHashMap<>();
            readings.put("Mattina", 100.0 + i * 5);
            readings.put("Pomeriggio", 110.0 + i * 5);
            bloodSugarData.put(today.plusDays(i), readings);
        }
    }

    private void setupCharts() {
        // serie di rilevazioni glicemiche
        morningSeries = new XYChart.Series<>();
        morningSeries.setName("Mattina");

        afternoonSeries = new XYChart.Series<>();
        afternoonSeries.setName("Pomeriggio");
        // serie di threshold
        minThresholdSeries = new XYChart.Series<>();
        minThresholdSeries.setName("Minima attenzione");

        maxThresholdSeries = new XYChart.Series<>();
        maxThresholdSeries.setName("Massima attenzione");

        // aggiornamento serie di rilevazioni glicemiche
        updateChartSeries();

        bloodSugarChart.getData().addAll(morningSeries, afternoonSeries, minThresholdSeries, maxThresholdSeries);
    }

    private void updateChartSeries() {
        morningSeries.getData().clear();
        afternoonSeries.getData().clear();
        minThresholdSeries.getData().clear();
        maxThresholdSeries.getData().clear();

        for (Map.Entry<LocalDate, Map<String, Double>> entry : bloodSugarData.entrySet()) {
            String dateLabel = entry.getKey().toString(); // ad es. "2025-06-01"
            Map<String, Double> readings = entry.getValue();

            if (readings.containsKey("Mattina")) {
                morningSeries.getData().add(new XYChart.Data<>(dateLabel, readings.get("Mattina")));
            }
            if (readings.containsKey("Pomeriggio")) {
                afternoonSeries.getData().add(new XYChart.Data<>(dateLabel, readings.get("Pomeriggio")));
            }
            minThresholdSeries.getData().add(new XYChart.Data<>(dateLabel, MIN_THRESHOLD));
            maxThresholdSeries.getData().add(new XYChart.Data<>(dateLabel, MAX_THRESHOLD));
        }
    }

    @FXML
    private void initializeChecklist() { // da lavorare
        String[] items = {"Controlla glicemia", "Assumi farmaco", "Fai attività fisica"};
        for (String item : items) {
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
                showAlert("Puoi inserire rilevazioni solo per oggi.");
                return;
            }

            String selectedSlot = timeSlotComboBox.getValue();
            if (selectedSlot == null) {
                showAlert("Seleziona una fascia oraria (Mattina o Pomeriggio).");
                return;
            }

            double reading = Double.parseDouble(readingField.getText());

            // Inizializza la mappa dei valori glicemici se necessario
            bloodSugarData.putIfAbsent(today, new LinkedHashMap<>());
            Map<String, Double> dailyReadings = bloodSugarData.get(today);

            boolean isModifying = dailyReadings.containsKey(selectedSlot);

            // Inizializza la mappa di modifiche per oggi
            modificationCountPerSlot.putIfAbsent(today, new HashMap<>());
            Map<String, Integer> slotModifications = modificationCountPerSlot.get(today);
            int slotModificationCount = slotModifications.getOrDefault(selectedSlot, 0);

            if (isModifying && slotModificationCount >= 1) {
                showAlert("Hai già modificato la fascia oraria: " + selectedSlot);
                return;
            }

            // Salva/modifica il dato
            dailyReadings.put(selectedSlot, reading);

            // Se è una modifica, aumenta il contatore della fascia
            if (isModifying) {
                slotModifications.put(selectedSlot, slotModificationCount + 1);
            }

            updateChartSeries();
            readingField.clear();
            timeSlotComboBox.getSelectionModel().clearSelection();
            updateAvailableTimeSlots();

        } catch (NumberFormatException ex) {
            showAlert("Inserisci un valore numerico valido.");
        }
    }

    @FXML
    private void handleLogout() {
        if (viewManager != null) {
            viewManager.logout();
        } else {
            showAlert("Errore nel gestore delle viste. Impossibile effettuare il logout.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
