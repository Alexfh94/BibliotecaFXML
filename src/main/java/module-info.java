module com.carballeira.practica1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.carballeira.practica1 to javafx.fxml, javafx.base;
    exports com.carballeira.practica1;

    exports com.carballeira.practica1.controller;
    opens com.carballeira.practica1.controller to javafx.fxml, javafx.base;
}