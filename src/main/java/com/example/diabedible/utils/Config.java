package com.example.diabedible.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Centralized application configuration loader.
 * Reads from application.properties on the classpath with sensible defaults.
 */
public final class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final String CONFIG_RESOURCE = "/application.properties";

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (in != null) {
                PROPS.load(in);
                LOGGER.info("Configuration loaded from {}", CONFIG_RESOURCE);
            } else {
                LOGGER.warn("Configuration resource {} not found. Using defaults.", CONFIG_RESOURCE);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load configuration from {}. Using defaults.", CONFIG_RESOURCE, e);
        }
    }

    private Config() { }

    private static String get(String key, String def) {
        return PROPS.getProperty(key, def);
    }

    public static int windowWidth() {
        return parseInt(get("app.window.width", "1200"), 1200);
    }

    public static int windowHeight() {
        return parseInt(get("app.window.height", "800"), 800);
    }

    public static int windowMinWidth() {
        return parseInt(get("app.window.minWidth", "800"), 800);
    }

    public static int windowMinHeight() {
        return parseInt(get("app.window.minHeight", "600"), 600);
    }

    public static boolean windowMaximized() {
        return Boolean.parseBoolean(get("app.window.maximized", "true"));
    }

    public static @NotNull String titlePrefix() {
        return get("app.title.prefix", "Diabedible - ");
    }

    public static @NotNull String loginTitle() {
        // If app.title.login contains a key like "title.login", ViewManager will localize it.
        // If it contains a literal like "Login", it will be used as-is.
        return get("app.title.login", "title.login");
    }

    public static @NotNull List<String> cssPaths() {
        String raw = get("app.css.paths", "/com/example/diabedible/styles.css");
        String[] split = raw.split(",");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            String trimmed = s.trim();
            if (!trimmed.isEmpty()) list.add(trimmed);
        }
        return list;
    }

    public static @NotNull String usersResourcePath() {
        return get("app.users.resource", "/com/example/diabedible/users.txt");
    }

    public static boolean demoEnabled() {
        return Boolean.parseBoolean(get("app.demo.enabled", "true"));
    }

    public static @NotNull String i18nBundleBaseName() {
        return get("app.i18n.bundle", "com.example.diabedible.i18n.messages");
    }

    public static @NotNull String localeTag() {
        return get("app.locale", "it-IT");
    }

    private static int parseInt(String value, int def) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
