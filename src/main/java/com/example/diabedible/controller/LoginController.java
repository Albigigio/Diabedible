package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.model.User;
import com.example.diabedible.service.LoginService;
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

    private final LoginService loginService;
    private ViewManager viewManager;

    public LoginController(LoginService loginService, ViewManager viewManager) {
        this.loginService = loginService;
        this.viewManager = viewManager;
    }

    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResource("/com/example/diabedible/Views/autenticazione/DiabedibileLogo.png").toExternalForm());
        logoImage.setImage(image);
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<User> userOpt = loginService.login(username, password);
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

        if (user.getUsername().startsWith("ID")) {
            fxmlPath = FXMLPaths.HOME_DIABETIC;
            title = "Home Paziente";
            // Utilizziamo il metodo standard del ViewManager
            // Ora il controller viene inizializzato automaticamente da FXML
            viewManager.switchScene(fxmlPath, title, 1200, 800, true);
        } else if (user.getUsername().startsWith("DR")) {
            fxmlPath = FXMLPaths.HOME_DOCTOR;
            title = "Home Diabetologo";
            viewManager.switchScene(fxmlPath, title, 1200, 800, true);
        } else {
            fxmlPath = FXMLPaths.HOME_ADMIN;
            title = "Home Admin";
            viewManager.switchScene(fxmlPath, title, 1200, 800, true);
        }
    }
}
