module com.example.diabedible {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;   

    // UI extra
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.jetbrains.annotations;

    // Logging
    requires org.slf4j;

    // Jackson (JSON)
    requires com.fasterxml.jackson.databind;    // ObjectMapper
    requires com.fasterxml.jackson.core;        // TypeReference, parser
    requires com.fasterxml.jackson.annotation;  // annotations

    // FXML
    opens com.example.diabedible to javafx.fxml;
    opens com.example.diabedible.controller to javafx.fxml;
    opens com.example.diabedible.utils to javafx.fxml;

    // Consenti a Jackson (e JavaFX) di riflettere sui model
    opens com.example.diabedible.model 
        to com.fasterxml.jackson.databind, javafx.base;

    // Export dei package pubblici
    exports com.example.diabedible;
    exports com.example.diabedible.controller;
    exports com.example.diabedible.utils;
    exports com.example.diabedible.model;
}
