package com.example.diabedible;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.utils.AlertUtils;
import com.example.diabedible.utils.Config;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            LOGGER.error("Uncaught exception on thread {}", t.getName(), e);
            AlertUtils.exception(
                    com.example.diabedible.utils.I18n.tr("error.app", "Errore dell'applicazione"),
                    com.example.diabedible.utils.I18n.tr("error.unhandled", "Si è verificato un errore non gestito"),
                    com.example.diabedible.utils.I18n.tr("error.app.unexpected", "Si è verificato un errore inatteso."),
                    e
            );
        });

        AppInjector injector = new AppInjector();
        ViewManager viewManager = new ViewManager(primaryStage, injector);

        try {
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
            AlertUtils.exception("Errore dell'applicazione", "Impossibile avviare l'app", "Errore durante l'avvio dell'applicazione.", e);
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
