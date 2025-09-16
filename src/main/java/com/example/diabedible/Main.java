package com.example.diabedible;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.service.LoginService;
import com.example.diabedible.utils.Config;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        ViewManager viewManager = new ViewManager(primaryStage);
        LoginService loginService = new LoginService();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLPaths.LOGIN));
            // Controller personalizzato con dipendenze
            LoginController loginController = new LoginController(loginService, viewManager);
            loader.setController(loginController);

            Scene scene = new Scene(loader.load(), Config.windowWidth(), Config.windowHeight());

            // Load CSS from centralized config
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
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle(Config.loginTitle());
            primaryStage.show();
        } catch (Exception e) {
            LOGGER.error("Errore all'avvio dell'applicazione", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
