package com.example.diabedible.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * Centralized helper for showing JavaFX alerts in a consistent way.
 * Ensures dialogs are shown on the JavaFX Application Thread.
 */
public final class AlertUtils {

    private AlertUtils() {}

    public static void info(String title, String header, String content) {
        show(Alert.AlertType.INFORMATION, title, header, content);
    }

    public static void warning(String title, String header, String content) {
        show(Alert.AlertType.WARNING, title, header, content);
    }

    public static void error(String title, String header, String content) {
        show(Alert.AlertType.ERROR, title, header, content);
    }

    public static void exception(String title, String header, String userMessage, Throwable throwable) {
        String details = toStackTrace(throwable);
        String content = (userMessage == null ? "" : userMessage) + (details == null ? "" : "\n\nDettagli:\n" + details);
        show(Alert.AlertType.ERROR, title, header, content);
    }

    private static void show(Alert.AlertType type, String title, String header, String content) {
        Runnable task = () -> {
            Alert alert = new Alert(type, Objects.requireNonNullElse(content, ""), ButtonType.OK);
            if (title != null) alert.setTitle(title);
            if (header != null) alert.setHeaderText(header);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            task.run();
        } else {
            Platform.runLater(task);
        }
    }

    private static String toStackTrace(Throwable t) {
        if (t == null) return null;
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
