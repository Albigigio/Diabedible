package com.example.diabedible.service;

import com.example.diabedible.model.User;
import com.example.diabedible.repository.UserRepository;
import com.example.diabedible.utils.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginService implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public @org.jetbrains.annotations.NotNull Optional<User> login(@org.jetbrains.annotations.NotNull String username, @org.jetbrains.annotations.NotNull String password) {
        LOGGER.debug("Tentativo login", org.slf4j.MarkerFactory.getMarker("AUTH"));
        try {
            return userRepository.findByUsername(username)
                    .filter(stored -> {
                        boolean match = HashUtils.verifyPassword(password, stored.passwordHash());
                        if (!match) {
                            LOGGER.warn("Credenziali non valide", org.slf4j.MarkerFactory.getMarker("AUTH"));
                        }
                        return match;
                    })
                    .map(stored -> {
                        // Upgrade legacy hashes to PBKDF2 upon successful login
                        if (HashUtils.needsUpgrade(stored.passwordHash())) {
                            try {
                                String newToken = HashUtils.createPasswordToken(password);
                                userRepository.save(new UserRepository.StoredUser(
                                        stored.id(), stored.username(), newToken, stored.role(), stored.displayName(), stored.metadata()
                                ));
                                LOGGER.info("Aggiornato hashing password a PBKDF2", org.slf4j.MarkerFactory.getMarker("AUTH"));
                            } catch (Exception e) {
                                LOGGER.warn("Impossibile aggiornare l'hashing della password", org.slf4j.MarkerFactory.getMarker("AUTH"), e);
                            }
                        }
                        LOGGER.info("Login riuscito", org.slf4j.MarkerFactory.getMarker("AUTH"));
                        return new User(stored.id(), stored.username(), stored.role(), stored.displayName(), stored.metadata());
                    });
        } catch (RuntimeException e) {
            LOGGER.error("Errore inatteso durante il login", org.slf4j.MarkerFactory.getMarker("AUTH"), e);
            throw new AuthenticationException("Errore durante l'autenticazione", e);
        }
    }
}
