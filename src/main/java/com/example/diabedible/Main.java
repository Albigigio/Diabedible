package com.example.diabedible;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.service.LoginService;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        ViewManager viewManager = new ViewManager(primaryStage);
        LoginService loginService = new LoginService();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLPaths.LOGIN));
            // Controller personalizzato con dipendenze
            LoginController loginController = new LoginController(loginService, viewManager);
            loader.setController(loginController);

            Scene scene = new Scene(loader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);

            // Load CSS
            String[] cssLocations = {
                "/com/example/diabedible/styles.css"
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
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
