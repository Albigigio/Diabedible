package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.model.Role;
import com.example.diabedible.model.User;
import com.example.diabedible.service.AuthService;
import com.example.diabedible.utils.FXMLPaths;
import com.example.diabedible.utils.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class LoginController implements ViewManaged {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private ImageView logoImage;
    @FXML private Button AccediButton;

    private final AuthService authService;
    private ViewManager viewManager;

    public LoginController(AuthService authService, ViewManager viewManager) {
        this.authService = authService;
        this.viewManager = viewManager;
    }

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/com/example/diabedible/Views/autenticazione/Logo2.png").toExternalForm());
        logoImage.setImage(image);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate inputs before calling the service
        if (!com.example.diabedible.utils.InputValidator.isValidUsername(username)) {
            messageLabel.setText("Username non valido. Usa 3-30 caratteri: lettere, numeri, punto, trattino, underscore.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }
        if (!com.example.diabedible.utils.InputValidator.isValidPassword(password)) {
            messageLabel.setText("Password non valida. Lunghezza 8-64 caratteri, soli caratteri ASCII stampabili.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        Optional<User> userOpt = authService.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            messageLabel.setText("Accesso consentito.");
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            switchToUserHome(user);
        } else {
            messageLabel.setText("Credenziali errate.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    private void switchToUserHome(User user) {
        String fxmlPath;
        String title;

        Role role = user.getRole();
        switch (role) {
            case DIABETIC -> {
                fxmlPath = FXMLPaths.HOME_DIABETIC;
                title = "Home Paziente";
                viewManager.switchScene(fxmlPath, title, 1200, 800, true);
            }
            case DOCTOR -> {
                fxmlPath = FXMLPaths.HOME_DOCTOR;
                title = "Home Diabetologo";
                viewManager.switchScene(fxmlPath, title, 1200, 800, true);
            }
            case ADMIN -> {
                fxmlPath = FXMLPaths.HOME_ADMIN;
                title = "Home Admin";
                viewManager.switchScene(fxmlPath, title, 1200, 800, true);
            }
            default -> {
                // fallback sicuro
                fxmlPath = FXMLPaths.HOME_DIABETIC;
                title = "Home";
                viewManager.switchScene(fxmlPath, title, 1200, 800, true);
            }
        }
    }
}
