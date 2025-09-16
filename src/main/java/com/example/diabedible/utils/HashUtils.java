package com.example.diabedible.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilities for password hashing and verification.
 *
 * Formats:
 * - PBKDF2 token: pbkdf2$sha256$<iterations>$<saltB64>$<hashB64>
 * - Legacy SHA-256 hex token (from users.txt): legacy-sha256$<hex>
 */
public class HashUtils {
    private static final String PBKDF2_ALG = "PBKDF2WithHmacSHA256";
    private static final int DEFAULT_ITERATIONS = 120_000;
    private static final int SALT_BYTES = 16; // 128-bit salt
    private static final int KEY_LEN_BITS = 256; // 256-bit derived key

    private static final SecureRandom RNG = new SecureRandom();

    private HashUtils() {}

    // ----- New API -----

    public static String createPasswordToken(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RNG.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt, DEFAULT_ITERATIONS, KEY_LEN_BITS);
        return encodePbkdf2(DEFAULT_ITERATIONS, salt, dk);
    }

    public static boolean verifyPassword(String password, String storedToken) {
        if (storedToken == null || storedToken.isEmpty()) return false;
        if (storedToken.startsWith("pbkdf2$")) {
            String[] parts = storedToken.split("\\$", 5);
            if (parts.length != 5) return false;
            int iterations = safeParseInt(parts[2], DEFAULT_ITERATIONS);
            byte[] salt = Base64.getDecoder().decode(parts[3]);
            byte[] expected = Base64.getDecoder().decode(parts[4]);
            byte[] actual = pbkdf2(password, salt, iterations, expected.length * 8);
            return MessageDigest.isEqual(expected, actual);
        } else if (storedToken.startsWith("legacy-sha256$")) {
            // Compare using legacy SHA-256 hex for backward compatibility
            String legacyHex = storedToken.substring("legacy-sha256$".length());
            String inputHex = sha256Hex(password);
            return constantTimeEqualsHex(legacyHex, inputHex);
        } else {
            // For safety, treat bare hex (old format without prefix) as legacy too
            if (isLikelyHex(storedToken)) {
                return constantTimeEqualsHex(storedToken, sha256Hex(password));
            }
            return false;
        }
    }

    public static boolean needsUpgrade(String storedToken) {
        return storedToken == null || storedToken.startsWith("legacy-sha256$") || isLikelyHex(storedToken);
    }

    // ----- Helpers -----

    private static byte[] pbkdf2(String password, byte[] salt, int iterations, int keyLenBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLenBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALG);
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Errore durante la derivazione PBKDF2", e);
        }
    }

    private static String encodePbkdf2(int iterations, byte[] salt, byte[] hash) {
        return String.format("pbkdf2$sha256$%d$%s$%s", iterations,
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hash));
    }

    private static int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    // Legacy SHA-256 hex utilities
    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Errore di generazione della password (SHA-256)", e);
        }
    }

    private static boolean isLikelyHex(String s) {
        if (s == null) return false;
        int len = s.length();
        if (len == 0 || (len % 2) != 0) return false;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            boolean hex = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
            if (!hex) return false;
        }
        return true;
    }

    private static boolean constantTimeEqualsHex(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= Character.toLowerCase(a.charAt(i)) ^ Character.toLowerCase(b.charAt(i));
        }
        return result == 0;
    }
}


