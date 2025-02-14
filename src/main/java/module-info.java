module com.carballeira.practica1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.sql;
    requires layout;
    requires kernel;


    opens com.carballeira.practica1 to javafx.fxml, javafx.base;
    exports com.carballeira.practica1;

    exports com.carballeira.practica1.controller;
    opens com.carballeira.practica1.controller to javafx.fxml, javafx.base;

    // Open the model package to javafx.base to allow reflection
    opens com.carballeira.practica1.model to javafx.base;

    // Export model package for testing purposes
    exports com.carballeira.practica1.model;
    exports com.carballeira.practica1.utils;
    opens com.carballeira.practica1.utils to javafx.base, javafx.fxml;
}
