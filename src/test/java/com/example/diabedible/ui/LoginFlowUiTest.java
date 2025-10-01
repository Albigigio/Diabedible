package com.example.diabedible.ui;

import com.example.diabedible.controller.LoginController;
import com.example.diabedible.di.AppInjector;
import com.example.diabedible.model.Role;
import com.example.diabedible.utils.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Minimal UI login flow test without external TestFX dependencies.
 * Runs only when -Dui.tests=true is provided.
 */
@EnabledIfSystemProperty(named = "ui.tests", matches = "true")
public class LoginFlowUiTest {

    @BeforeAll
    static void initToolkit() {
        try {
            new javafx.embed.swing.JFXPanel();
        } catch (Throwable ignored) {
        }
    }

    @Test
    void loginValidAndInvalid() throws Exception {
        // Prepare FX UI
        CountDownLatch uiLatch = new CountDownLatch(1);
        final Stage[] stageRef = new Stage[1];
        final LoginController[] controllerRef = new LoginController[1];

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stageRef[0] = stage;

            AppInjector injector = new AppInjector();
            ViewManager vm = new ViewManager(stage, injector);

            LoginController controller = injector.createLoginController(vm);
            controllerRef[0] = controller;

            try {
                FXMLLoader loader = new FXMLLoader(LoginFlowUiTest.class.getResource(FXMLPaths.LOGIN), I18n.bundle());
                loader.setController(controller);
                Scene scene = new Scene(loader.load(), Config.windowWidth(), Config.windowHeight());
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            uiLatch.countDown();
        });
        assertTrue(uiLatch.await(5, TimeUnit.SECONDS), "FX UI setup timed out");

        // INVALID: too short username
        CountDownLatch invalidLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage stage = stageRef[0];
            Scene scene = stage.getScene();

            TextField user = (TextField) scene.lookup("#usernameField");
            PasswordField pass = (PasswordField) scene.lookup("#passwordField");
            Label message = (Label) scene.lookup("#messageLabel");
            Button loginBtn = (Button) scene.lookup("#AccediButton");

            user.setText("ab"); // too short to be valid
            pass.setText("whatever");
            loginBtn.fire();

            assertTrue(message.getText().toLowerCase().contains("username"));
            invalidLatch.countDown();
        });
        assertTrue(invalidLatch.await(5, TimeUnit.SECONDS), "Invalid login assertion timed out");

        // VALID: demo credentials seeded by DemoDataProvider
        AppSession.clear();
        CountDownLatch validLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage stage = stageRef[0];
            Scene scene = stage.getScene();

            TextField user = (TextField) scene.lookup("#usernameField");
            PasswordField pass = (PasswordField) scene.lookup("#passwordField");
            Button loginBtn = (Button) scene.lookup("#AccediButton");

            user.setText("IDMario");
            pass.setText("123456");
            loginBtn.fire();

            validLatch.countDown();
        });
        assertTrue(validLatch.await(5, TimeUnit.SECONDS), "Valid login action timed out");

        // Allow navigation to complete
        Thread.sleep(300);

        assertNotNull(AppSession.getCurrentUser(), "User should be set in session after valid login");
        assertEquals(Role.DIABETIC, AppSession.getCurrentUser().getRole());
        assertNotNull(stageRef[0].getScene(), "Scene should be present after navigation");
        assertNotNull(stageRef[0].getScene().getRoot(), "Scene root should be present after navigation");
    }
}
