package com.example.diabedible.utils;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.di.AppInjector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Minimal JavaFX navigation test without external TestFX deps.
 * It runs only when -Dui.tests=true is provided to avoid headless issues in CI until configured.
 */
@EnabledIfSystemProperty(named = "ui.tests", matches = "true")
public class ViewManagerNavigationTest {

    @BeforeAll
    static void initToolkit() {
        try {
            // Initialize JavaFX toolkit once
            new javafx.embed.swing.JFXPanel();
        } catch (Throwable ignored) {
        }
    }

    @Test
    void navigateToLogin_loadsScene() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        final Stage[] stageRef = new Stage[1];
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stageRef[0] = stage;
            AppInjector injector = new AppInjector();
            ViewManager vm = new ViewManager(stage, injector);
            LoginController controller = injector.createLoginController(vm);
            vm.switchSceneWithController(
                    FXMLPaths.LOGIN,
                    controller,
                    Config.loginTitle(),
                    Config.windowWidth(),
                    Config.windowHeight(),
                    Config.windowMaximized()
            );
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS), "FX action did not complete in time");

        // Wait a little for scene to be applied
        Thread.sleep(200);

        Stage stage = stageRef[0];
        assertNotNull(stage, "Stage should be created");
        assertNotNull(stage.getScene(), "Scene should be set on stage");
        assertNotNull(stage.getScene().getRoot(), "Scene root should not be null");
        assertTrue(stage.getTitle() != null && !stage.getTitle().isBlank(), "Window title should be set");
    }
}
