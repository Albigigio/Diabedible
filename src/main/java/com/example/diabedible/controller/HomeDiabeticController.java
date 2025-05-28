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
import java.util.HashMap;
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
    @FXML private LineChart<Number, Number> bloodSugarChart;
    @FXML private TextField readingField;
    @FXML private DatePicker datePicker;

    // Controller per la home del paziente diabetico
    private final Map<LocalDate, Double> bloodSugarData = new HashMap<>();
    private final Map<LocalDate, Double> activityData = new HashMap<>();

    @FXML
    public void initialize() {
        initializeSampleData();
        setupCharts();
        setupEventHandlers();
    }

    private void initializeSampleData() {
        LocalDate today = LocalDate.now();
        bloodSugarData.put(today.minusDays(6), 145.0);
        bloodSugarData.put(today.minusDays(5), 132.0);
        // altri dati?
    }
    // setup grafici
    private void setupCharts() {
        XYChart.Series<Number, Number> series = createChartSeries(bloodSugarData, "Blood Sugar");
        bloodSugarChart.getData().add(series);
    }

    private XYChart.Series<Number, Number> createChartSeries(Map<LocalDate, Double> data, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);

        int dayCount = 1;
        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(dayCount, entry.getValue()));
            dayCount++;
        }

        return series;
    }

    private void setupEventHandlers() {
        // Example for add reading button
        // Note: This should be wired in FXML using onAction="#handleAddReading"
    }

    @FXML
    private void handleAddReading() {
        try {
            double reading = Double.parseDouble(readingField.getText());
            LocalDate date = datePicker.getValue();
            bloodSugarData.put(date, reading);
            bloodSugarChart.getData().clear();
            bloodSugarChart.getData().add(createChartSeries(bloodSugarData, "Zuccheri nel sangue"));
            readingField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Inserisci un valore valido per i livelli di zucchero.");
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