package com.example.diabedible.controller;

import com.example.diabedible.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.*;
import com.example.diabedible.utils.HashUtils;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    //Mappa delle coppie (username/password) caricate dal file
    private final Map<String, String> userMap = new HashMap<>();

    //Inizializzazione controller
    @FXML
    public void initialize() {
        loadUsersFromFile(); //Messo in resources
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hashed = HashUtils.hashPassword(password);   //Calcolo hash

        //Verifica se l'username esiste e se la password corrisponde
        if (userMap.containsKey(username) && userMap.get(username).equals(hashed)) {
            messageLabel.setText("Accesso consentito.");    //Login sì :)
            //Verifica se paziente, dottore o admin(?)
            if (username.startsWith("ID")) {
                Main.switchScene("home-diabetic.fxml", "Home Paziente");
            } else if (username.startsWith("DR")) {
                Main.switchScene("home-doctor.fxml", "Home Diabetologo");
            } else {
                // pagina admin o altri usi
                Main.switchScene("admin-home.fxml", "Home Admin");
            }
        } else {
            messageLabel.setText("Credenziali errate.");    //Login no :(
        }
    }

    //Carica le credenziali utenti dal file users.txt nella cartella resources
    private void loadUsersFromFile() {
        try (Scanner scanner = new Scanner(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/example/diabedible/users.txt"))
                ))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    userMap.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            //In caso di errore nel caricamento del file, mostra errore
            messageLabel.setText("Errore file utenti");
        }
    }
}
