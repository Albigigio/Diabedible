package com.example.diabedible.repository;

import com.example.diabedible.model.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Repository abstraction for user credentials and roles.
 * This hides the underlying persistence/storage from services.
 */
public interface UserRepository {

    /**
     * Immutable record representing a stored user with credential hash.
     */
    record StoredUser(@NotNull String id,
                      @NotNull String username,
                      @NotNull String passwordHash,
                      @NotNull Role role,
                      @NotNull String displayName,
                      @Nullable Map<String, String> metadata) {}

    @NotNull Optional<StoredUser> findByUsername(@NotNull String username);

    /**
     * Saves or replaces a user.
     */
    void save(@NotNull StoredUser user);

    /**
     * Returns all stored users.
     */
    @NotNull Collection<StoredUser> findAll();
}
