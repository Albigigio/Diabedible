package com.example.diabedible.controller;

import com.example.diabedible.Main;
import com.example.diabedible.model.User;
import com.example.diabedible.service.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final LoginService loginService = new LoginService();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<User> userOpt = loginService.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            messageLabel.setText("Accesso consentito.");
            // Switch scena in base al tipo di utente
            if (user.getUsername().startsWith("ID")) {
                Main.switchScene("diabetic/home-diabetic.fxml", "Home Paziente", 1200, 800);
            } else if (user.getUsername().startsWith("DR")) {
                Main.switchScene("doctor/home-doctor.fxml", "Home Diabetologo", 1200, 800);
            } else {
                Main.switchScene("admin/admin-home.fxml", "Home Admin", 1200, 800);
            }
        } else {
            messageLabel.setText("Credenziali errate.");
        }
    }
}
