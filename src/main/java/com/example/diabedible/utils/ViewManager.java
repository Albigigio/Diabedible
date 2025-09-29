package com.example.diabedible.utils;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.di.AppInjector;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ViewManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewManager.class);
    private final Stage primaryStage;
    private final AppInjector injector;

    public ViewManager(@NotNull Stage stage, @NotNull AppInjector injector) {
        this.primaryStage = stage;
        this.injector = injector;
        stage.setMinWidth(Config.windowMinWidth());
        stage.setMinHeight(Config.windowMinHeight());
    }

    public void switchScene(@NotNull String fxmlPath, @NotNull String title, int width, int height, boolean maximize) {
        Runnable task = () -> {
            try {
                var url = getClass().getResource(fxmlPath);
                if (url == null) {
                    LOGGER.error("FXML non trovato: {}", fxmlPath);
                    AlertUtils.error(
                            I18n.tr("error.resource.missing.title", "Risorsa mancante"),
                            I18n.tr("error.view.not.found", "Vista non trovata"),
                            I18n.tr("error.view.missing.detail", "Impossibile trovare la vista: ") + fxmlPath
                    );
                    return;
                }
                ResourceBundle bundle = I18n.bundle();
                FXMLLoader loader = new FXMLLoader(url, bundle);
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
                Scene scene = new Scene(loader.load(), width, height);

                // Carica i fogli di stile CSS da diverse posizioni
                loadStylesheets(scene);

                Object controller = loader.getController();
                if (controller instanceof ViewManaged) {
                    ((ViewManaged) controller).setViewManager(this);
                }

                primaryStage.setScene(scene);
                String appName = I18n.tr("app.name", Config.titlePrefix().replace(" - ", ""));
                String resolvedTitle = I18n.tr(title, title);
                primaryStage.setTitle(appName + " - " + resolvedTitle);
                primaryStage.setMaximized(maximize);

                if (!primaryStage.isShowing()) {
                    primaryStage.show();
                }

            } catch (IOException e) {
                LOGGER.error("Errore nel caricamento della vista: {}", fxmlPath, e);
                AlertUtils.exception(
                        I18n.tr("error.app", "Errore dell'applicazione"),
                        I18n.tr("error.unhandled", "Si è verificato un errore"),
                        I18n.tr("error.view.load", "Errore nel caricamento della vista: ") + fxmlPath,
                        e
                );
            }
        };
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }

    public void switchSceneWithController(@NotNull String fxmlPath, @NotNull Object controller, @NotNull String title, int width, int height, boolean maximize) {
        Runnable task = () -> {
            try {
                ResourceBundle bundle = I18n.bundle();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath), bundle);
                loader.setController(controller);

                Scene scene = new Scene(loader.load(), width, height);

                // Carica i fogli di stile CSS da diverse posizioni
                loadStylesheets(scene);

                primaryStage.setScene(scene);
                String appName = I18n.tr("app.name", Config.titlePrefix().replace(" - ", ""));
                String resolvedTitle = I18n.tr(title, title);
                primaryStage.setTitle(appName + " - " + resolvedTitle);
                primaryStage.setMaximized(maximize);

                if (!primaryStage.isShowing()) {
                    primaryStage.show();
                }

            } catch (IOException e) {
                LOGGER.error("Errore nel caricamento della vista con controller personalizzato: {}", fxmlPath, e);
                AlertUtils.exception(
                        I18n.tr("error.app", "Errore dell'applicazione"),
                        I18n.tr("error.unhandled", "Si è verificato un errore"),
                        I18n.tr("error.view.load.custom", "Errore nel caricamento della vista con controller personalizzato: ") + fxmlPath,
                        e
                );
            }
        };
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }

    /**
     * Expose DI-provided services when needed by controllers.
     */
    public com.example.diabedible.service.LogoutService getLogoutService() {
        return injector.getLogoutService();
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
