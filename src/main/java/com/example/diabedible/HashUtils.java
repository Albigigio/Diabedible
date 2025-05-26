package com.example.diabedible;

import java.security.MessageDigest;

public class HashUtils {
<<<<<<< Updated upstream
    private HashUtils() {} //Costruttore privato per impedire istanziazione

    /**
     * Genera hash SHA-256 di una password usando codifica UTF-8
     * @param password password da hashare
     * @return stringa hash
     */

=======
>>>>>>> Stashed changes
    public static String hashPassword(String password) {
        try {
            //Uso SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
<<<<<<< Updated upstream
            
            //Calcolo hash con UTF-8
            byte[] hashed = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            //Corstruttore stringa esadecimale
=======
            byte[] hash = md.digest(password.getBytes());
>>>>>>> Stashed changes
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


