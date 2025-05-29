package com.example.diabedible.controller;

import com.example.diabedible.Main;
import com.example.diabedible.model.User;
import com.example.diabedible.service.LoginService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private ImageView logoImage;
    @FXML private Button AccediButton;

    private final LoginService loginService = new LoginService();

    @FXML
    public void initialize() {
        //Caricamento logo
        Image image = new Image(getClass().getResource("/com/example/diabedible/Views/autenticazione/DiabedibileLogo.png").toExternalForm());
        logoImage.setImage(image);
    }




    @FXML
    public void handleLogin() {
//        AccediButton.setStyle("-fx-background-color: #7849a4; -fx-text-fill: white;");    //Cambio colore alla pressione?
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<User> userOpt = loginService.login(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            messageLabel.setText("Accesso consentito.");

            // Switch scena in base al tipo di utente
            if (user.getUsername().startsWith("ID")) {
                Main.switchScene("Views/diabetic/home-diabetic.fxml", "Home Paziente", 1200, 800);
            } else if (user.getUsername().startsWith("DR")) {
                Main.switchScene("Views/doctor/home-doctor.fxml", "Home Diabetologo", 1200, 800);
            } else {
                Main.switchScene("Views/Admin/home-admin.fxml", "Home Admin", 1200, 800);
            }
        } else {
            messageLabel.setText("Credenziali errate.");
        }
    }
}
