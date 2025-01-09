module com.carballeira.practica1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;

    opens com.carballeira.practica1 to javafx.fxml, javafx.base;
    exports com.carballeira.practica1;

    exports com.carballeira.practica1.controller;
    opens com.carballeira.practica1.controller to javafx.fxml, javafx.base;

    // Open the model package to javafx.base to allow reflection
    opens com.carballeira.practica1.model to javafx.base;
}
