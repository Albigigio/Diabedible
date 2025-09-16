package com.example.diabedible.repository;

import com.example.diabedible.model.Role;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository abstraction for user credentials and roles.
 * This hides the underlying persistence/storage from services.
 */
public interface UserRepository {

    /**
     * Immutable record representing a stored user with credential hash.
     */
    record StoredUser(String username, String passwordHash, Role role) {}

    Optional<StoredUser> findByUsername(String username);

    /**
     * Saves or replaces a user.
     */
    void save(StoredUser user);

    /**
     * Returns all stored users.
     */
    Collection<StoredUser> findAll();
}
