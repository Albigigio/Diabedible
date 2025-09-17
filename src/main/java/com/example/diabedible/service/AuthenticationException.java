package com.example.diabedible.service;

/**
 * Thrown when an unexpected error occurs during the authentication process
 * (e.g., hashing failure, repository malfunction). It is not used for
 * invalid credentials, which are represented by an empty Optional result.
 */
public class AuthenticationException extends AppException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
