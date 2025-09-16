package com.example.diabedible.service;

import com.example.diabedible.model.Role;
import com.example.diabedible.model.User;
import com.example.diabedible.utils.Config;
import com.example.diabedible.utils.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.*;

public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private static class UserRecord {
        final String hash;
        final Role role;
        UserRecord(String hash, Role role) {
            this.hash = hash;
            this.role = role;
        }
    }

    private final Map<String, UserRecord> users = new HashMap<>();

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
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                String[] parts = trimmed.split(":", 3);
                if (parts.length == 3) {
                    String username = parts[0];
                    String hash = parts[1];
                    String roleStr = parts[2].toUpperCase(Locale.ITALY).trim();
                    try {
                        Role role = Role.valueOf(roleStr);
                        users.put(username, new UserRecord(hash, role));
                        LOGGER.debug("Caricato utente: {} con ruolo {}", username, role);
                    } catch (IllegalArgumentException iae) {
                        LOGGER.warn("Ruolo non valido '{}' per utente '{}'. Riga ignorata.", roleStr, username);
                    }
                } else {
                    LOGGER.warn("Formato riga utenti non valido: '{}'. Atteso: username:hash:ROLE", trimmed);
                }
            }
            LOGGER.info("Utenti caricati: {}", users.size());
        } catch (Exception e) {
            LOGGER.error("Errore nel caricamento utenti", e);
        }
    }

    public Optional<User> login(String username, String password) {
        String hashedInput = HashUtils.hashPassword(password);
        LOGGER.debug("Tentativo login per username='{}'", username);
        UserRecord rec = users.get(username);
        LOGGER.trace("Hash calcolato: {} | Hash atteso: {}", hashedInput, rec == null ? "<null>" : rec.hash);
        if (rec != null && rec.hash.equals(hashedInput)) {
            LOGGER.info("Login riuscito per username='{}'", username);
            return Optional.of(new User(username, rec.role));
        }
        LOGGER.warn("Login fallito per username='{}'", username);
        return Optional.empty();
    }
}
