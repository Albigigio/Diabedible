package com.example.diabedible.utils;

import com.example.diabedible.model.User;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application session holding the current authenticated user and shared state.
 */
public final class AppSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppSession.class);
    private static volatile User currentUser;

    private AppSession() { }

    public static void setCurrentUser(@Nullable User user) {
        currentUser = user;
        if (user != null) {
            String name = user.getDisplayName();
            LOGGER.info("Sessione avviata per utente: {} [{}] ({})", name, user.getUsername(), user.getRole());
        } else {
            LOGGER.info("Sessione utente impostata a null");
        }
    }

    public static @Nullable User getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
        LOGGER.info("Sessione utente pulita");
    }
}
