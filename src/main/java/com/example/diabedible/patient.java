package com.example.diabedible;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class patient extends Application {

    private final Map<LocalDate, Double> bloodSugarData = new HashMap<>();
    private final Map<LocalDate, Double> medicationData = new HashMap<>();
    private final Map<LocalDate, Double> activityData = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Initialize with sample data
        initializeSampleData();

        // Main container
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Top section - Welcome and notifications
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Center - Main content
        TabPane mainContent = createMainContent();
        root.setCenter(mainContent);

        // Left - Navigation
        VBox navigation = createNavigation();
        root.setLeft(navigation);

        // Right - Quick stats
        VBox quickStats = createQuickStats();
        root.setRight(quickStats);

        // Set up the scene
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Diabetic Patient Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 20, 20));
        topBar.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 10;");

        // Welcome message
        Text welcomeText = new Text("Welcome back, John!");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeText.setFill(Color.WHITE);

        // Notification icon
        ImageView notificationIcon = new ImageView(new Image(getClass().getResourceAsStream("/notification.png")));
        notificationIcon.setFitHeight(30);
        notificationIcon.setFitWidth(30);
        Button notificationBtn = new Button("", notificationIcon);
        notificationBtn.setStyle("-fx-background-color: transparent;");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(welcomeText, spacer, notificationBtn);
        return topBar;
    }

    private TabPane createMainContent() {
        TabPane tabPane = new TabPane();

        // Blood Sugar Tab
        Tab bloodSugarTab = new Tab("Blood Sugar", createBloodSugarContent());
        bloodSugarTab.setClosable(false);

        // Medication Tab
        Tab medicationTab = new Tab("Medication", createMedicationContent());
        medicationTab.setClosable(false);

        // Activity Tab
        Tab activityTab = new Tab("Activity", createActivityContent());
        activityTab.setClosable(false);

        // Nutrition Tab
        Tab nutritionTab = new Tab("Nutrition", createNutritionContent());
        nutritionTab.setClosable(false);

        tabPane.getTabs().addAll(bloodSugarTab, medicationTab, activityTab, nutritionTab);
        return tabPane;
    }

    private VBox createBloodSugarContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Title
        Text title = new Text("Blood Sugar Monitoring");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Chart
        LineChart<Number, Number> bloodSugarChart = createBloodSugarChart();

        // Add reading form
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);

        Label readingLabel = new Label("Blood Sugar (mg/dL):");
        TextField readingField = new TextField();

        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label timeLabel = new Label("Time:");
        ComboBox<String> timeCombo = new ComboBox<>();
        timeCombo.getItems().addAll("Morning", "Afternoon", "Evening", "Night");
        timeCombo.setValue("Morning");

        Label notesLabel = new Label("Notes:");
        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(2);

        Button addButton = new Button("Add Reading");
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addButton.setOnAction(_ -> {
            try {
                double reading = Double.parseDouble(readingField.getText());
                LocalDate date = datePicker.getValue();
                bloodSugarData.put(date, reading);
                // In a real app, you would save this to a database
                // Then refresh the chart
                bloodSugarChart.getData().clear();
                bloodSugarChart.getData().add(createChartSeries(bloodSugarData, "Blood Sugar"));
                readingField.clear();
                notesArea.clear();
            } catch (NumberFormatException ex) {
                showAlert("Please enter a valid number for blood sugar reading.");
            }
        });

        form.add(readingLabel, 0, 0);
        form.add(readingField, 1, 0);
        form.add(dateLabel, 0, 1);
        form.add(datePicker, 1, 1);
        form.add(timeLabel, 0, 2);
        form.add(timeCombo, 1, 2);
        form.add(notesLabel, 0, 3);
        form.add(notesArea, 1, 3);
        form.add(addButton, 1, 4);

        content.getChildren().addAll(title, bloodSugarChart, form);
        return content;
    }

    private LineChart<Number, Number> createBloodSugarChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(50, 300, 25);
        xAxis.setLabel("Days");
        yAxis.setLabel("Blood Sugar (mg/dL)");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Blood Sugar Trends");
        lineChart.setPrefHeight(300);

        XYChart.Series<Number, Number> series = createChartSeries(bloodSugarData, "Blood Sugar");
        lineChart.getData().add(series);

        return lineChart;
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

    private VBox createMedicationContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Text title = new Text("Medication Tracker");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Medication list
        ListView<String> medicationList = new ListView<>();
        medicationList.getItems().addAll(
                "Metformin - 500mg - Morning & Evening",
                "Insulin Glargine - 10 units - Night",
                "Empagliflozin - 10mg - Morning"
        );

        // Add medication form
        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);

        Label nameLabel = new Label("Medication Name:");
        TextField nameField = new TextField();

        Label doseLabel = new Label("Dosage:");
        TextField doseField = new TextField();

        Label frequencyLabel = new Label("Frequency:");
        ComboBox<String> frequencyCombo = new ComboBox<>();
        frequencyCombo.getItems().addAll("Once daily", "Twice daily", "Three times daily", "As needed");

        Button addButton = new Button("Add Medication");
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addButton.setOnAction(_ -> {
            String medication = nameField.getText() + " - " + doseField.getText() + " - " + frequencyCombo.getValue();
            medicationList.getItems().add(medication);
            nameField.clear();
            doseField.clear();
        });

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(doseLabel, 0, 1);
        form.add(doseField, 1, 1);
        form.add(frequencyLabel, 0, 2);
        form.add(frequencyCombo, 1, 2);
        form.add(addButton, 1, 3);

        content.getChildren().addAll(title, medicationList, form);
        return content;
    }

    private VBox createActivityContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Text title = new Text("Physical Activity");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Activity chart
        LineChart<Number, Number> activityChart = createActivityChart();

        // Activity log
        TextArea activityLog = new TextArea();
        activityLog.setPrefRowCount(5);
        activityLog.setText("Monday: 30 min walk\nTuesday: 15 min yoga\nWednesday: 45 min cycling");

        content.getChildren().addAll(title, activityChart, new Label("Recent Activities:"), activityLog);
        return content;
    }

    private LineChart<Number, Number> createActivityChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(0, 60, 10);
        xAxis.setLabel("Days");
        yAxis.setLabel("Activity Minutes");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Daily Activity");
        lineChart.setPrefHeight(300);

        XYChart.Series<Number, Number> series = createChartSeries(activityData, "Activity Minutes");
        lineChart.getData().add(series);

        return lineChart;
    }

    private VBox createNutritionContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Text title = new Text("Nutrition Tracker");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Daily nutrition summary
        GridPane summary = new GridPane();
        summary.setVgap(10);
        summary.setHgap(20);

        summary.add(new Label("Calories:"), 0, 0);
        summary.add(new Label("1800 kcal"), 1, 0);

        summary.add(new Label("Carbohydrates:"), 0, 1);
        summary.add(new Label("210 g"), 1, 1);

        summary.add(new Label("Proteins:"), 0, 2);
        summary.add(new Label("85 g"), 1, 2);

        summary.add(new Label("Fats:"), 0, 3);
        summary.add(new Label("60 g"), 1, 3);

        // Meal log
        TextArea mealLog = new TextArea();
        mealLog.setPrefRowCount(5);
        mealLog.setText("Breakfast: Oatmeal, berries, almonds\nLunch: Grilled chicken, quinoa, vegetables\nDinner: Salmon, sweet potato, broccoli");

        content.getChildren().addAll(title, summary, new Label("Today's Meals:"), mealLog);
        return content;
    }

    private VBox createNavigation() {
        VBox nav = new VBox(15);
        nav.setPadding(new Insets(20));
        nav.setStyle("-fx-background-color: #34495e; -fx-background-radius: 10;");
        nav.setPrefWidth(200);

        // Profile section
        ImageView profileImage = new ImageView(new Image(getClass().getResourceAsStream("/profile.png")));
        profileImage.setFitHeight(80);
        profileImage.setFitWidth(80);
        profileImage.setPreserveRatio(true);

        Text profileName = new Text("John Doe");
        profileName.setFill(Color.WHITE);
        profileName.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text profileType = new Text("Type 2 Diabetes");
        profileType.setFill(Color.LIGHTGRAY);

        VBox profileBox = new VBox(5, profileImage, profileName, profileType);
        profileBox.setAlignment(Pos.CENTER);

        // Navigation buttons
        Button dashboardBtn = createNavButton("Dashboard", "/home.png");
        Button bloodSugarBtn = createNavButton("Blood Sugar", "/glucose.png");
        Button medicationBtn = createNavButton("Medication", "/meds.png");
        Button activityBtn = createNavButton("Activity", "/activity.png");
        Button nutritionBtn = createNavButton("Nutrition", "/food.png");
        Button appointmentsBtn = createNavButton("Appointments", "/calendar.png");
        Button settingsBtn = createNavButton("Settings", "/settings.png");

        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        nav.getChildren().addAll(profileBox, new Separator(), dashboardBtn, bloodSugarBtn,
                medicationBtn, activityBtn, nutritionBtn, appointmentsBtn,
                new Separator(), settingsBtn, logoutBtn);

        return nav;
    }

    private Button createNavButton(String text, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitHeight(20);
        icon.setFitWidth(20);

        Button button = new Button(text, icon);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: center-left;");
        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    private VBox createQuickStats() {
        VBox stats = new VBox(15);
        stats.setPadding(new Insets(20));
        stats.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 10;");
        stats.setPrefWidth(250);

        Text statsTitle = new Text("Quick Stats");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Today's blood sugar
        VBox bloodSugarBox = createStatBox("Today's Glucose", "126 mg/dL", "Within target range", Color.GREEN);

        // Last A1C
        VBox a1cBox = createStatBox("Last A1C", "6.2%", "Good control", Color.GREEN);

        // Medication adherence
        VBox adherenceBox = createStatBox("Medication Adherence", "92%", "Excellent", Color.GREEN);

        // Activity
        VBox activityBox = createStatBox("Weekly Activity", "4.5 hours", "On track", Color.GREEN);

        // Reminders
        VBox remindersBox = new VBox(5);
        remindersBox.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 10;");

        Text remindersTitle = new Text("Reminders");
        remindersTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        CheckBox medReminder = new CheckBox("Take Metformin - 9:00 AM");
        CheckBox checkSugar = new CheckBox("Check blood sugar - 2:00 PM");
        CheckBox exercise = new CheckBox("30 min walk - 6:00 PM");

        medReminder.setSelected(true);

        remindersBox.getChildren().addAll(remindersTitle, medReminder, checkSugar, exercise);

        stats.getChildren().addAll(statsTitle, bloodSugarBox, a1cBox, adherenceBox, activityBox, remindersBox);
        return stats;
    }

    private VBox createStatBox(String title, String value, String status, Color color) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 10;");

        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Text statusText = new Text(status);
        statusText.setFill(color);

        box.getChildren().addAll(titleText, valueText, statusText);
        return box;
    }

    private void initializeSampleData() {
        // Sample blood sugar data (last 7 days)
        LocalDate today = LocalDate.now();
        bloodSugarData.put(today.minusDays(6), 145.0);
        bloodSugarData.put(today.minusDays(5), 132.0);
        bloodSugarData.put(today.minusDays(4), 128.0);
        bloodSugarData.put(today.minusDays(3), 119.0);
        bloodSugarData.put(today.minusDays(2), 126.0);
        bloodSugarData.put(today.minusDays(1), 138.0);
        bloodSugarData.put(today, 126.0);

        // Sample activity data
        activityData.put(today.minusDays(6), 25.0);
        activityData.put(today.minusDays(5), 30.0);
        activityData.put(today.minusDays(4), 45.0);
        activityData.put(today.minusDays(3), 20.0);
        activityData.put(today.minusDays(2), 35.0);
        activityData.put(today.minusDays(1), 40.0);
        activityData.put(today, 30.0);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}