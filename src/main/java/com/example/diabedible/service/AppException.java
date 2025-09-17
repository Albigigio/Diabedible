package com.example.diabedible.service;

/**
 * Base application exception for expected service-layer errors.
 * UI can catch this type to show user-friendly messages.
 */
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
