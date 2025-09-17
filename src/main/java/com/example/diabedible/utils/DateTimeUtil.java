package com.example.diabedible.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility class for date/time formatting normalized across the app.
 */
public final class DateTimeUtil {

    private DateTimeUtil() {}

    public static DateTimeFormatter dateFormatter(Locale locale) {
        // Use a common localized pattern: e.g., 17/09/2025 for it-IT, 9/17/2025 for en-US
        return DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.SHORT).withLocale(locale);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(dateFormatter(Locale.getDefault()));
    }
}
