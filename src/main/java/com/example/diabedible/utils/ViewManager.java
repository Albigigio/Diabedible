package com.example.diabedible.utils;

import com.example.diabedible.ViewManaged;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {
    private final Stage primaryStage;

    public ViewManager(Stage stage) {
        this.primaryStage = stage;
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }

    public void switchScene(String fxmlPath, String title, int width, int height, boolean maximize) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), width, height);

            // Carica i fogli di stile CSS da diverse posizioni
            loadStylesheets(scene);

            Object controller = loader.getController();
            if (controller instanceof ViewManaged) {
                ((ViewManaged) controller).setViewManager(this);
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Diabedible - " + title);
            primaryStage.setMaximized(maximize);

            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }

        } catch (IOException e) {
            showErrorAlert("Errore nel caricamento della vista: " + fxmlPath, e);
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore dell'applicazione");
        alert.setHeaderText("Si è verificato un errore");
        alert.setContentText(message + "\n\nDettagli: " + e.getMessage());
        alert.showAndWait();
    }

    public void switchToLoginScene() {
        switchScene(FXMLPaths.LOGIN, "Login", 1200, 800, true);
    }

    public void switchToDiabeticDashboard() {
        switchScene(FXMLPaths.HOME_DIABETIC, "Dashboard Paziente", 1200, 800, true);
    }

    public void switchToDoctorDashboard() {
        switchScene(FXMLPaths.HOME_DOCTOR, "Dashboard Diabetologo", 1200, 800, true);
    }

    public void switchToAdminDashboard() {
        switchScene(FXMLPaths.HOME_ADMIN, "Dashboard Admin", 1200, 800, true);
    }

    public void switchSceneWithController(String fxmlPath, Object controller, String title, int width, int height, boolean maximize) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);

            Scene scene = new Scene(loader.load(), width, height);

            // Carica i fogli di stile CSS da diverse posizioni
            loadStylesheets(scene);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Diabedible - " + title);
            primaryStage.setMaximized(maximize);

            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }

        } catch (IOException e) {
            showErrorAlert("Errore nel caricamento della vista con controller personalizzato: " + fxmlPath, e);
            e.printStackTrace();
        }
    }

    public void logout() {
        switchToLoginScene();
    }

    private void loadStylesheets(Scene scene) {
        // Prova a caricare da diverse posizioni comuni
        String[] cssLocations = {
            "/com/example/diabedible/styles.css",
            "/css/styles.css",
            "/styles.css"
        };

        boolean cssLoaded = false;

        for (String location : cssLocations) {
            try {
                if (getClass().getResource(location) != null) {
                    scene.getStylesheets().add(getClass().getResource(location).toExternalForm());
                    System.out.println("CSS caricato da: " + location);
                    cssLoaded = true;
                    break;
                }
            } catch (Exception e) {
                System.err.println("Errore nel caricamento del CSS da " + location + ": " + e.getMessage());
            }
        }

        if (!cssLoaded) {
            System.err.println("Impossibile caricare il CSS da nessuna posizione conosciuta");
            // Applica stili base direttamente
            applyBasicStyles(scene);
        }
    }

    private void applyBasicStyles(Scene scene) {
        String basicStyles = ".nav-button {" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-alignment: center-left;" +
                "-fx-max-width: infinity;" +
                "}";
        scene.getStylesheets().add("data:text/css," + basicStyles.replace(" ", "%20"));
    }
}
