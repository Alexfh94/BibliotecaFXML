package com.carballeira.practica1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


public class LoginController {

    @FXML
    private TextField idUsuario;
    @FXML
    private PasswordField contraseña;
    @FXML
    private TextField contraseñaVisible;
    @FXML
    private Button inicioSesion;
    @FXML
    private Button botonVolver;
    @FXML
    private Button mostrarContraseña;
    @FXML
    private ProgressBar progressBar;

    private String administrador;
    private String password;
    private int contador = 0;



    @FXML
    public void initialize() {
        mostrarContraseña.setDisable(true);
        contraseña.textProperty().addListener((observable, oldValue, newValue) -> actualizarEstadoBoton());
    }

    private void actualizarEstadoBoton() {
        if (contraseña.getText().isEmpty()) {
            mostrarContraseña.setDisable(true);
        } else {
            mostrarContraseña.setDisable(false);
        }
    }

    @FXML
    private void inicioSesionAction(ActionEvent event) {
        if (idUsuario.getText().isEmpty() || contraseña.getText().isEmpty()) {
            showError("Error", "ID o contraseña incorrectos.");
        } else {
            new Thread(this::iniciarSesion).start();
        }
    }

    private void iniciarSesion() {

    }

    public void mostrarContraseñaAction(ActionEvent event) {
        // Alternar entre mostrar y ocultar la contraseña
        if (contador % 2 == 0) {
            // Mostrar contraseña: establecer el TextField visible y el PasswordField invisible
            contraseñaVisible.setText(contraseña.getText());
            contraseña.setVisible(false);
            contraseñaVisible.setVisible(true);
            mostrarContraseña.setText("Ocultar");
        } else {
            // Ocultar contraseña: volver a mostrar el PasswordField y ocultar el TextField
            contraseña.setText(contraseñaVisible.getText());
            contraseña.setVisible(true);
            contraseñaVisible.setVisible(false);
            mostrarContraseña.setText("Mostrar");
        }
        contador++;
    }

    @FXML
    private void botonVolverAction(ActionEvent event) {

    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
