package com.example.diabedible;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.nio.file.*;
import java.util.*;
import com.example.diabedible.HashUtils;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final Map<String, String> userMap = new HashMap<>();

    @FXML
    public void initialize() {
        loadUsersFromFile(); // messo in resources
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashed = HashUtils.hashPassword(password);

        if (userMap.containsKey(username) && userMap.get(username).equals(hashed)) {
            messageLabel.setText("Accesso consentito.");
        } else {
            messageLabel.setText("Credenziali errate.");
        }
    }

    private void loadUsersFromFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(Objects.requireNonNull(getClass().getResource("/com/example/diabedible/users.txt")).toURI()));
            for (String line : lines) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    userMap.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            messageLabel.setText("Errore file utenti");
        }
    }
}
