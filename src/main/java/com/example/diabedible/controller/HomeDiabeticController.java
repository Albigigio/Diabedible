package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.utils.AlertUtils;
import com.example.diabedible.utils.DateTimeUtil;
import com.example.diabedible.utils.ViewManager;
import com.example.diabedible.viewmodel.HomeDiabeticViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
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

    // ViewModel encapsulating domain and business logic
    private final HomeDiabeticViewModel viewModel = new HomeDiabeticViewModel();
    private XYChart.Series<String, Number> morningSeries;
    private XYChart.Series<String, Number> afternoonSeries;
    private XYChart.Series<String, Number> minThresholdSeries;
    private XYChart.Series<String, Number> maxThresholdSeries;

    @FXML
    public void initialize() {
        initializeSampleData();
        setupCharts();

        
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

         var user = com.example.diabedible.utils.AppSession.getCurrentUser();
    if (user != null) {
        var intakeService = AppInjector.getIntakeServiceStatic();
        var adherenceService = AppInjector.getAdherenceAlertServiceStatic();

        String username = user.getUsername();
        LocalDate today = LocalDate.now();

        if (adherenceService.shouldAlertPatientToday(username)) {

             boolean hasAnyIntake = intakeService.hasAnyIntakeOnDate(username, today);
             String message = hasAnyIntake
            ? "Oggi hai registrato solo parte delle assunzioni prescritte."
            : "Oggi non risulta registrata alcuna assunzione dei farmaci prescritti.";

            AlertUtils.warning(
            "Promemoria terapia",
            null,
            message + "\nRicordati di registrare correttamente le assunzioni."
            );
        }

    }
}

    private void updateAvailableTimeSlots() {
        timeSlotComboBox.getItems().clear();
        LocalDate today = LocalDate.now();
        List<String> slots = viewModel.availableSlotsFor(today);
        timeSlotComboBox.getItems().addAll(slots);
    }

    private void initializeSampleData() {
        viewModel.initSampleData();
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

        for (Map.Entry<LocalDate, Map<String, Double>> entry : viewModel.asMapForChart().entrySet()) {
            String dateLabel = DateTimeUtil.formatDate(entry.getKey());
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
        LocalDate selectedDate = datePicker.getValue();
        String selectedSlot = timeSlotComboBox.getValue();
        String readingText = readingField.getText();

        HomeDiabeticViewModel.AddResult result = viewModel.addOrModifyReading(selectedDate, selectedSlot, readingText);
        switch (result) {
            case REJECTED_NOT_TODAY -> showAlert(ALERT_ONLY_TODAY);
            case REJECTED_SLOT_NULL -> showAlert(ALERT_SELECT_SLOT);
            case REJECTED_INVALID_NUMBER -> showAlert(ALERT_INVALID_NUMBER);
            case REJECTED_TOO_MANY_MODS -> showAlert(ALERT_ALREADY_MODIFIED_PREFIX + selectedSlot);
            case ADDED, MODIFIED -> {
                updateChartSeries();
                readingField.clear();
                timeSlotComboBox.getSelectionModel().clearSelection();
                updateAvailableTimeSlots();
            }
        }
    }

    @FXML
    private void handleLogout() {
        if (viewManager != null) {
            viewManager.getLogoutService().logout(viewManager);
        } else {
            showAlert(ALERT_LOGOUT_ERROR);
        }
    }

    private void showAlert(String message) {
        AlertUtils.warning(ALERT_TITLE_WARNING, null, message);
    }


    @FXML
private void onAddSymptomClicked() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/com/example/diabedible/Views/diabetic/symptom-entry.fxml"));
    Parent root = loader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.setTitle("Nuovo Sintomo");
    stage.show();
}

}
