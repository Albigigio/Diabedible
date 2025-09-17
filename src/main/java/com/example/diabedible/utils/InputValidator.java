package com.example.diabedible.utils;

import java.util.regex.Pattern;

/**
 * Simple input validation utilities for common UI fields.
 */
public final class InputValidator {

    private static final Pattern USERNAME_ALLOWED = Pattern.compile("^[A-Za-z0-9._-]+$");
    // Printable ASCII from space (0x20) to tilde (0x7E)
    private static final Pattern PASSWORD_ALLOWED = Pattern.compile("^[\\x20-\\x7E]+$");

    private InputValidator() { }

    public static boolean isNonEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isLengthBetween(String s, int minInclusive, int maxInclusive) {
        if (s == null) return false;
        int len = s.length();
        return len >= minInclusive && len <= maxInclusive;
    }

    public static boolean matchesAllowedChars(String s, Pattern allowed) {
        return s != null && allowed.matcher(s).matches();
    }

    // Convenience rules for this app
    public static boolean isValidUsername(String username) {
        return isNonEmpty(username)
                && isLengthBetween(username, 3, 30)
                && matchesAllowedChars(username, USERNAME_ALLOWED);
    }

    public static boolean isValidPassword(String password) {
        return isNonEmpty(password)
                && isLengthBetween(password, 6, 64)
                && matchesAllowedChars(password, PASSWORD_ALLOWED);
    }
}
