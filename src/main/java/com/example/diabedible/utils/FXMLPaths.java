package com.example.diabedible.utils;

public class FXMLPaths {
    // Percorso base delle viste
    private static final String BASE_PATH = "/com/example/diabedible/Views/";
    // Percorso di tutte le viste
    public static final String LOGIN = BASE_PATH + "autenticazione/login-view.fxml";
    public static final String HOME_DIABETIC = BASE_PATH + "diabetic/home-diabetic.fxml";
    public static final String HOME_DOCTOR = BASE_PATH + "doctor/home-doctor.fxml";
    public static final String HOME_ADMIN = BASE_PATH + "Admin/home-admin.fxml";

    private FXMLPaths() {
        //costruttore privato per impedire istanziazione
    }
}
