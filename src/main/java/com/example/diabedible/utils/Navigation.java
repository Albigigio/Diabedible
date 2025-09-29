package com.example.diabedible.utils;

import com.example.diabedible.model.Role;

/**
 * Central navigation metadata.
 */
public enum Navigation {
    LOGIN(FXMLPaths.LOGIN, "Login", Config.windowWidth(), Config.windowHeight(), Config.windowMaximized()),
    HOME_DIABETIC(FXMLPaths.HOME_DIABETIC, "Home Paziente", 1200, 800, true),
    HOME_DOCTOR(FXMLPaths.HOME_DOCTOR, "Home Diabetologo", 1200, 800, true),
    HOME_ADMIN(FXMLPaths.HOME_ADMIN, "Home Admin", 1200, 800, true);

    public final String fxml;
    public final String title;
    public final int width;
    public final int height;
    public final boolean maximize;

    Navigation(String fxml, String title, int width, int height, boolean maximize) {
        this.fxml = fxml;
        this.title = title;
        this.width = width;
        this.height = height;
        this.maximize = maximize;
    }

    public static Navigation forRole(Role role) {
        return switch (role) {
            case DIABETIC -> HOME_DIABETIC;
            case DOCTOR -> HOME_DOCTOR;
            case ADMIN -> HOME_ADMIN;
        };
    }
}
