package com.example.diabedible.repository;

import com.example.diabedible.model.Role;
import com.example.diabedible.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * In-memory implementation of UserRepository. It can migrate/seed users
 * from the legacy flat file (users.txt) on startup for a smooth transition.
 */
public class InMemoryUserRepository implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<String, StoredUser> users = new HashMap<>();

    public InMemoryUserRepository() {
        migrateFromLegacyUsersFile();
    }

    private void migrateFromLegacyUsersFile() {
        try {
            String usersResource = Config.usersResourcePath();
            URL resource = getClass().getResource(usersResource);
            if (resource == null) {
                LOGGER.info("Nessun file legacy utenti trovato ({}). Repository avviato vuoto.", usersResource);
                return;
            }
            List<String> lines = Files.readAllLines(Paths.get(resource.toURI()));
            int imported = 0;
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
                        users.put(username, new StoredUser(username, hash, role));
                        imported++;
                    } catch (IllegalArgumentException iae) {
                        LOGGER.warn("Ruolo non valido '{}' per utente '{}'. Riga ignorata.", roleStr, username);
                    }
                } else {
                    LOGGER.warn("Formato riga utenti non valido: '{}'. Atteso: username:hash:ROLE", trimmed);
                }
            }
            LOGGER.info("Migrazione utenti legacy completata. Importati: {}", imported);
        } catch (Exception e) {
            LOGGER.error("Errore durante la migrazione dal file legacy utenti", e);
        }
    }

    @Override
    public Optional<StoredUser> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void save(StoredUser user) {
        users.put(user.username(), user);
    }

    @Override
    public Collection<StoredUser> findAll() {
        return Collections.unmodifiableCollection(users.values());
    }
}
