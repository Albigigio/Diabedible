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
        String hashedInput = HashUtils.hashPassword(password);
        LOGGER.debug("Tentativo login per username='{}'", username);
        return userRepository.findByUsername(username)
                .filter(stored -> {
                    boolean match = stored.passwordHash().equals(hashedInput);
                    if (!match) {
                        LOGGER.warn("Hash non corrispondente per username='{}'", username);
                    }
                    return match;
                })
                .map(stored -> {
                    LOGGER.info("Login riuscito per username='{}'", username);
                    return new User(username, stored.role());
                });
    }
}
