package com.example.diabedible.utils;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.controller.LoginController;
import com.example.diabedible.service.LoginService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ViewManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewManager.class);
    private final Stage primaryStage;

    public ViewManager(Stage stage) {
        this.primaryStage = stage;
        stage.setMinWidth(Config.windowMinWidth());
        stage.setMinHeight(Config.windowMinHeight());
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
            primaryStage.setTitle(Config.titlePrefix() + title);
            primaryStage.setMaximized(maximize);

            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }

        } catch (IOException e) {
            LOGGER.error("Errore nel caricamento della vista: {}", fxmlPath, e);
            showErrorAlert("Errore nel caricamento della vista: " + fxmlPath, e);
        }
    }

    private void showErrorAlert(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore dell'applicazione");
        alert.setHeaderText("Si è verificato un errore");
        alert.setContentText(message + "\n\nDettagli: " + e.getMessage());
        alert.showAndWait();
    }

    public void switchSceneWithController(String fxmlPath, Object controller, String title, int width, int height, boolean maximize) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);

            Scene scene = new Scene(loader.load(), width, height);

            // Carica i fogli di stile CSS da diverse posizioni
            loadStylesheets(scene);

            primaryStage.setScene(scene);
            primaryStage.setTitle(Config.titlePrefix() + title);
            primaryStage.setMaximized(maximize);

            if (!primaryStage.isShowing()) {
                primaryStage.show();
            }

        } catch (IOException e) {
            LOGGER.error("Errore nel caricamento della vista con controller personalizzato: {}", fxmlPath, e);
            showErrorAlert("Errore nel caricamento della vista con controller personalizzato: " + fxmlPath, e);
        }
    }

    public void logout() {
        LoginController loginController = new LoginController(new LoginService(), this);
        switchSceneWithController(FXMLPaths.LOGIN, loginController, Config.loginTitle(), Config.windowWidth(), Config.windowHeight(), Config.windowMaximized());
    }

    private void loadStylesheets(Scene scene) {
        boolean cssLoaded = false;
        for (String location : Config.cssPaths()) {
            try {
                if (getClass().getResource(location) != null) {
                    scene.getStylesheets().add(getClass().getResource(location).toExternalForm());
                    LOGGER.info("CSS caricato da: {}", location);
                    cssLoaded = true;
                    break;
                }
            } catch (Exception e) {
                LOGGER.warn("Errore nel caricamento del CSS da {}", location, e);
            }
        }

        if (!cssLoaded) {
            LOGGER.warn("Impossibile caricare il CSS da nessuna posizione conosciuta");
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
