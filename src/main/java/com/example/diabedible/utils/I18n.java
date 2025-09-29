package com.example.diabedible.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Simple i18n helper that loads a ResourceBundle based on Config settings
 * and provides safe lookups with defaults.
 */
public final class I18n {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18n.class);
    private static volatile ResourceBundle BUNDLE;

    private I18n() {}

    public static ResourceBundle bundle() {
        ResourceBundle local = BUNDLE;
        if (local == null) {
            synchronized (I18n.class) {
                if (BUNDLE == null) {
                    try {
                        Locale locale = Locale.forLanguageTag(Config.localeTag());
                        BUNDLE = ResourceBundle.getBundle(Config.i18nBundleBaseName(), locale);
                        LOGGER.info("Loaded i18n bundle {} for locale {}", Config.i18nBundleBaseName(), locale);
                    } catch (MissingResourceException ex) {
                        LOGGER.warn("i18n bundle not found: {} (locale={}). Falling back to empty bundle.", Config.i18nBundleBaseName(), Config.localeTag());
                        BUNDLE = new EmptyResourceBundle();
                    }
                }
                local = BUNDLE;
            }
        }
        return local;
    }

    public static String tr(String key, String def) {
        try {
            return bundle().getString(key);
        } catch (MissingResourceException ex) {
            return def;
        }
    }

    /**
     * Very small empty bundle to avoid null checks.
     */
    private static final class EmptyResourceBundle extends ResourceBundle {
        @Override
        protected Object handleGetObject(String key) { return null; }
        @Override
        public boolean containsKey(String key) { return false; }
        @Override
        public java.util.Enumeration<String> getKeys() { return java.util.Collections.emptyEnumeration(); }
    }
}
