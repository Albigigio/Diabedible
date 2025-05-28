package com.example.diabedible.utils;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class HashUtils {
    private HashUtils() {} //Costruttore privato per impedire istanziazione

    /**
     * Genera hash SHA-256 di una password usando codifica UTF-8
     * @param password password da hashare
     * @return stringa hash
     */
    public static String hashPassword(String password) {
        try {
            //Uso SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //Calcolo hash con UTF-8
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            //Errore più specifico, non mi piaceva RuntimeException
            throw new IllegalStateException("Errore di generazione della password", e);
        }
    }
}


