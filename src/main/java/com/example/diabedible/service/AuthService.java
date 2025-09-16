package com.example.diabedible.service;

import com.example.diabedible.model.User;

import java.util.Optional;

/**
 * Authentication service abstraction.
 * Controllers should depend on this interface rather than concrete implementations.
 */
public interface AuthService {
    Optional<User> login(String username, String password);
}
