package com.example.diabedible.utils;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.controller.LoginController;
import com.example.diabedible.di.AppInjector;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ViewManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewManager.class);
    private final Stage primaryStage;
    private final AppInjector injector;

    public ViewManager(Stage stage, AppInjector injector) {
        this.primaryStage = stage;
        this.injector = injector;
        stage.setMinWidth(Config.windowMinWidth());
        stage.setMinHeight(Config.windowMinHeight());
    }

    public void switchScene(String fxmlPath, String title, int width, int height, boolean maximize) {
        Runnable task = () -> {
            try {
                var url = getClass().getResource(fxmlPath);
                if (url == null) {
                    LOGGER.error("FXML non trovato: {}", fxmlPath);
                    AlertUtils.error("Risorsa mancante", "Vista non trovata", "Impossibile trovare la vista: " + fxmlPath);
                    return;
                }
                FXMLLoader loader = new FXMLLoader(url);
                // Controller factory: let injector handle known controllers, otherwise default
                loader.setControllerFactory(type -> {
                    try {
                        // For now, no specific controllers via injector except LoginController created elsewhere
                        return type.getDeclaredConstructor().newInstance();
                    } catch (Exception ex) {
                        LOGGER.error("Impossibile creare controller {}", type, ex);
                        throw new RuntimeException(ex);
                    }
                });
                // Pass resource bundle if available in future
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
                AlertUtils.exception("Errore dell'applicazione", "Si è verificato un errore", "Errore nel caricamento della vista: " + fxmlPath, e);
            }
        };
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }

    public void switchSceneWithController(String fxmlPath, Object controller, String title, int width, int height, boolean maximize) {
        Runnable task = () -> {
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
                AlertUtils.exception("Errore dell'applicazione", "Si è verificato un errore", "Errore nel caricamento della vista con controller personalizzato: " + fxmlPath, e);
            }
        };
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }

    public void logout() {
        Runnable task = () -> {
            // clear session
            AppSession.clear();
            LoginController loginController = injector.createLoginController(this);
            switchSceneWithController(FXMLPaths.LOGIN, loginController, Config.loginTitle(), Config.windowWidth(), Config.windowHeight(), Config.windowMaximized());
        };
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
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
