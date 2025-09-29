package com.example.diabedible.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class User {
    private final @NotNull String id;
    private final @NotNull String username;
    private final @NotNull Role role;
    private final @NotNull String displayName;
    private final @Nullable Map<String, String> metadata;

    public User(@NotNull String id, @NotNull String username, @NotNull Role role, @NotNull String displayName, @Nullable Map<String, String> metadata) {
        this.id = Objects.requireNonNull(id, "id");
        this.username = Objects.requireNonNull(username, "username");
        this.role = Objects.requireNonNull(role, "role");
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.metadata = metadata == null ? null : Collections.unmodifiableMap(metadata);
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull Role getRole() {
        return role;
    }

    public @NotNull String getDisplayName() {
        return displayName;
    }

    public @Nullable Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
