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
    public Optional<User> login(String username, String password) {
        LOGGER.debug("Tentativo login per username='{}'", username);
        return userRepository.findByUsername(username)
                .filter(stored -> {
                    boolean match = HashUtils.verifyPassword(password, stored.passwordHash());
                    if (!match) {
                        LOGGER.warn("Credenziali non valide per username='{}'", username);
                    }
                    return match;
                })
                .map(stored -> {
                    // Upgrade legacy hashes to PBKDF2 upon successful login
                    if (HashUtils.needsUpgrade(stored.passwordHash())) {
                        try {
                            String newToken = HashUtils.createPasswordToken(password);
                            userRepository.save(new UserRepository.StoredUser(stored.username(), newToken, stored.role()));
                            LOGGER.info("Aggiornato hashing password a PBKDF2 per username='{}'", username);
                        } catch (Exception e) {
                            LOGGER.warn("Impossibile aggiornare l'hashing della password per username='{}'", username, e);
                        }
                    }
                    LOGGER.info("Login riuscito per username='{}'", username);
                    return new User(username, stored.role());
                });
    }
}
