module com.example.diabedible {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    // Logging
    requires org.slf4j;

    opens com.example.diabedible to javafx.fxml;
    exports com.example.diabedible;
    exports com.example.diabedible.controller;
    opens com.example.diabedible.controller to javafx.fxml;
    exports com.example.diabedible.utils;
    opens com.example.diabedible.utils to javafx.fxml;
}
