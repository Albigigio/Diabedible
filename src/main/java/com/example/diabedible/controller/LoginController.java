package com.example.diabedible.controller;

import com.example.diabedible.ViewManaged;
import com.example.diabedible.model.Role;
import com.example.diabedible.model.User;
import com.example.diabedible.service.AppException;
import com.example.diabedible.service.AuthService;
import com.example.diabedible.utils.AlertUtils;
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

        // Demo hints for testers
        if (usernameField != null) {
            usernameField.setPromptText("Username demo: IDMario (o DRGiulia, Admin1)");
        }
        if (passwordField != null) {
            passwordField.setPromptText("Password demo: 123456");
        }
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
            messageLabel.setText("Password non valida. Lunghezza 6-64 caratteri, soli caratteri ASCII stampabili.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        try {
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
        } catch (AppException ex) {
            AlertUtils.error("Errore di autenticazione", "Impossibile completare il login", ex.getMessage());
        } catch (RuntimeException ex) {
            AlertUtils.exception("Errore dell'applicazione", "Si è verificato un errore", "Errore inatteso durante il login.", ex);
        }
    }

    private void switchToUserHome(User user) {
        // set session
        com.example.diabedible.utils.AppSession.setCurrentUser(user);
        Role role = user.getRole();
        com.example.diabedible.utils.Navigation nav = com.example.diabedible.utils.Navigation.forRole(role);
        viewManager.switchScene(nav.fxml, nav.title, nav.width, nav.height, nav.maximize);
    }
}
