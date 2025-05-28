package com.example.diabedible.service;

import com.example.diabedible.model.User;
import com.example.diabedible.utils.HashUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LoginService {
    private final Map<String, String> userMap = new HashMap<>();

    public LoginService() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get(Objects.requireNonNull(getClass().getResource("/com/example/diabedible/users.txt")).toURI())
            );
            System.out.println("Debug - Numero di linee lette: " + lines.size());
            for (String line : lines) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    userMap.put(parts[0], parts[1]);
                    System.out.println("Debug - Caricato utente: " + parts[0]);
                }
            }
            System.out.println("Debug - Utenti caricati: " + userMap.size());
        } catch (Exception e) {
            System.err.println("Errore nel caricamento utenti: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Optional<User> login(String username, String password) {
        String hashedInput = HashUtils.hashPassword(password);
        System.out.println("Debug - Username inserito: '" + username + "'");
        System.out.println("Debug - Hash calcolato: " + hashedInput);
        System.out.println("Debug - Hash nel file: " + userMap.get(username));
        System.out.println("Debug - Username trovato: " + userMap.containsKey(username));
        if (userMap.containsKey(username) && userMap.get(username).equals(hashedInput)) {
            return Optional.of(new User(username));
        }
        return Optional.empty();
    }
}