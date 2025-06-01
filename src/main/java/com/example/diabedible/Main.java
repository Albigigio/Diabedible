package com.example.diabedible;

import com.example.diabedible.utils.FXMLPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage mainStage;
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;


    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        mainStage.setMinWidth(800);
        mainStage.setMinHeight(600);

        switchToLoginScene();
    }
    // Metodi per cambiare la scena
    public static void switchToLoginScene() {
        switchScene(FXMLPaths.LOGIN, "Login", DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
    }
    // Metodi per cambiare la scena di dashboard
    public static void switchToDiabeticDashboard() {
        switchScene(FXMLPaths.HOME_DIABETIC, "Dashboard Paziente", DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
    }

    public static void switchToDoctorDashboard() {
        switchScene(FXMLPaths.HOME_DOCTOR, "Dashboard Diabetologo", DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
    }

    public static void switchToAdminDashboard() {
        switchScene(FXMLPaths.HOME_ADMIN, "Dashboard Admin", DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
    }
    // Metodi per cambiare la scena di un determinato fxml
    public static void switchScene(String fxmlPath, String title, int width, int height, boolean maximize) {
        if (mainStage == null) {
            System.err.println("Errore: mainStage non è stato inizializzato.");
            return;
        }
        // Caricamento della vista
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), width, height);

            mainStage.setScene(scene);
            mainStage.setTitle("Diabedible - " + title);
            mainStage.setMaximized(maximize);

            if (!mainStage.isShowing()) {
                mainStage.show();
            }
        } catch (Exception e) {
            showErrorAlert("Errore nel caricamento della vista: " + fxmlPath, e);
            e.printStackTrace();
        }
    }
    // Metodi per effettuare il logout
    public static void logout() {
        switchToLoginScene();
    }
    // Metodi per mostrare un alert di errore
    private static void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore dell'applicazione");
        alert.setHeaderText("Si è verificato un errore");
        alert.setContentText(message + "\n\nDettagli: " + e.getMessage());
        alert.showAndWait();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
