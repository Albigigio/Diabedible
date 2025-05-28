package com.example.diabedible;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage mainStage;
    private static final String RESOURCE_PATH = "/com/example/diabedible/Views/";
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        mainStage.setMinWidth(800);
        mainStage.setMinHeight(600);
        switchToLoginScene();
    }

    public static void switchToLoginScene() {
        switchScene("autenticazione/login-view.fxml", "Login", DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static void switchToDiabeticDashboard() {
        switchScene("diabetic/home-diabetic.fxml", "Dashboard Paziente", DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public static void switchScene(String fxmlFile, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(RESOURCE_PATH + fxmlFile));
            Scene scene = new Scene(loader.load(), width, height);

            mainStage.setScene(scene);
            mainStage.setTitle(title);

            if (!mainStage.isShowing()) {
                mainStage.show();
            }
        } catch (Exception e) {
            showErrorAlert("Failed to load view: " + fxmlFile, e);
            e.printStackTrace();
        }
    }

    private static void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message + "\n\nError details: " + e.getMessage());
        alert.showAndWait();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}