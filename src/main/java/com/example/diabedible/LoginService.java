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
            for (String line : lines) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    userMap.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nel caricamento utenti: " + e.getMessage());
        }
    }

    public Optional<User> login(String username, String password) {
        String hashedInput = HashUtils.hashPassword(password);
        if (userMap.containsKey(username) && userMap.get(username).equals(hashedInput)) {
            return Optional.of(new User(username));
        }
        return Optional.empty();
    }
}
