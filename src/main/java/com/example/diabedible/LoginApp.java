package com.example.diabedible;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class LoginApp extends Application {

    //Mappa utenti
    private final Map<String, String> userMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        //Caricamento utenti dal file
        loadUsersFromFile("users.txt");

        /// UI ///
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        //Al clic
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String hashedInput = hashPassword(password);

            //Verifica credenziali
            if (userMap.containsKey(username) && userMap.get(username).equals(hashedInput)) {
                messageLabel.setText("Accesso consentito.");
            } else {
                messageLabel.setText("Credenziali errate.");
            }
        });

        //Griglia dell'interfaccia
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(messageLabel, 1, 3);

        //Creazione della finestra
        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Accedi");
        primaryStage.show();
    }

    //Caricare gli utenti da file (username:password_hash)
    private void loadUsersFromFile(String filename) {
        try {
            for (String line : Files.readAllLines(Paths.get(filename))) {
                String[] parts = line.split(":", 2); //Divide la linea in username e password hashata
                if (parts.length == 2) {
                    userMap.put(parts[0], parts[1]); //Inserisce nella mappa
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    //Generare l'hash SHA-256 della password
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes()); //Calcola l'hash
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b)); //Converte ogni byte in formato esadecimale
            }
            return sb.toString(); //Restituisce la stringa hashata
        } catch (Exception e) {
            throw new RuntimeException(e); //In caso di errore, Exception
        }
    }

    //Avvio JavaFX
    public static void main(String[] args) {
        launch(args);
    }
}

