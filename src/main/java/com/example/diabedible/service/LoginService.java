package com.example.diabedible.service;

import com.example.diabedible.model.User;
import com.example.diabedible.utils.Config;
import com.example.diabedible.utils.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.*;

public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    private final Map<String, String> userMap = new HashMap<>();

    public LoginService() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            String usersResource = Config.usersResourcePath();
            List<String> lines = Files.readAllLines(
                    Paths.get(Objects.requireNonNull(getClass().getResource(usersResource)).toURI())
            );
            LOGGER.debug("Numero di linee lette: {}", lines.size());
            for (String line : lines) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    userMap.put(parts[0], parts[1]);
                    LOGGER.debug("Caricato utente: {}", parts[0]);
                }
            }
            LOGGER.info("Utenti caricati: {}", userMap.size());
        } catch (Exception e) {
            LOGGER.error("Errore nel caricamento utenti", e);
        }
    }

    public Optional<User> login(String username, String password) {
        String hashedInput = HashUtils.hashPassword(password);
        LOGGER.debug("Tentativo login per username='{}'", username);
        LOGGER.trace("Hash calcolato: {} | Hash atteso: {}", hashedInput, userMap.get(username));
        if (userMap.containsKey(username) && userMap.get(username).equals(hashedInput)) {
            LOGGER.info("Login riuscito per username='{}'", username);
            return Optional.of(new User(username));
        }
        LOGGER.warn("Login fallito per username='{}'", username);
        return Optional.empty();
    }
}
