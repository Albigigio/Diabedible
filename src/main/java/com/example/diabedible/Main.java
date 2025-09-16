package com.example.diabedible;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.utils.Config;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        AppInjector injector = new AppInjector();
        ViewManager viewManager = new ViewManager(primaryStage, injector);

        try {
            // Delegate initial scene creation and CSS loading to ViewManager
            LoginController loginController = injector.createLoginController(viewManager);
            viewManager.switchSceneWithController(
                    FXMLPaths.LOGIN,
                    loginController,
                    Config.loginTitle(),
                    Config.windowWidth(),
                    Config.windowHeight(),
                    Config.windowMaximized()
            );
        } catch (Exception e) {
            LOGGER.error("Errore all'avvio dell'applicazione", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
