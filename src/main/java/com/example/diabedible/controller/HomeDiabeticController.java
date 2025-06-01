package com.example.diabedible.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class HomeDiabeticController {

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

    // Mappa annidata
    private final Map<LocalDate, Map<String, Double>> bloodSugarData = new LinkedHashMap<>();
    private XYChart.Series<String, Number> bloodSugarSeries;

    @FXML
    public void initialize() {
        initializeSampleData();
        setupCharts();

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

        // Inizializza comboBox
        timeSlotComboBox.getItems().addAll("Mattina", "Pomeriggio");
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
        bloodSugarSeries = new XYChart.Series<>();
        bloodSugarSeries.setName("Zuccheri nel sangue");

        updateChartSeries();
        bloodSugarChart.getData().add(bloodSugarSeries);
    }

    private void updateChartSeries() {
        bloodSugarSeries.getData().clear();

        for (Map.Entry<LocalDate, Map<String, Double>> entry : bloodSugarData.entrySet()) {
            LocalDate date = entry.getKey();
            Map<String, Double> readings = entry.getValue();

            readings.forEach((fascia, valore) -> {
                String label = date.toString() + " (" + fascia + ")";
                bloodSugarSeries.getData().add(new XYChart.Data<>(label, valore));
            });
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

            bloodSugarData.putIfAbsent(today, new LinkedHashMap<>());
            Map<String, Double> dailyReadings = bloodSugarData.get(today);

            if (dailyReadings.containsKey(selectedSlot)) {
                showAlert("Hai già inserito una rilevazione per la fascia " + selectedSlot + ".");
                return;
            }

            double reading = Double.parseDouble(readingField.getText());
            dailyReadings.put(selectedSlot, reading);

            updateChartSeries();

            readingField.clear();
            timeSlotComboBox.getSelectionModel().clearSelection();
        } catch (NumberFormatException ex) {
            showAlert("Inserisci un valore numerico valido.");
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
