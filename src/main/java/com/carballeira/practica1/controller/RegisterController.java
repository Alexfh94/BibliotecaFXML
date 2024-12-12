package com.carballeira.practica1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

public class RegisterController {

    @FXML
    private TextField nombre, email, telefono;

    @FXML
    private PasswordField contraseña;

    @FXML
    private TextField contraseñaVisible;

    @FXML
    private Button crearUsuario, botonVolver, mostrarContraseña;

    private int contador = 0;

    @FXML
    private void initialize() {
        mostrarContraseña.setDisable(true);

        contraseña.textProperty().addListener((observable, oldValue, newValue) -> actualizarEstadoBoton());

        nombre.setOnAction(this::crearUsuario);
        email.setOnAction(this::crearUsuario);
        telefono.setOnAction(this::crearUsuario);
        contraseña.setOnAction(this::crearUsuario);

        // Acción de tecla ESC para volver
        botonVolver.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                botonVolver.fire();
            }
        });

        botonVolver.setOnAction(event -> botonVolverAction());
    }

    @FXML
    private void crearUsuario(ActionEvent event) {
        String nombre1 = nombre.getText();
        String email1 = email.getText();
        String telefono1 = telefono.getText();
        String contraseña1 = contraseña.getText();

        if (validarCampos(nombre1, email1, telefono1, contraseña1)) {
           //TODO ABRIR VENTANA DE CAPTCHA

        }
    }

    private boolean validarCampos(String nombre, String email, String telefono, String contraseña) {
        if (nombre.isEmpty()) {
            showAlert("Error", "El nombre no puede estar vacío.");
            return false;
        }
        if (!email.contains("@")) {
            showAlert("Error", "El formato del email es incorrecto.");
            return false;
        }
        if (!telefono.matches("[1-9]+") || telefono.length() != 9) {
            showAlert("Error", "El teléfono debe tener 9 dígitos y solo contener números.");
            return false;
        }
        if (contraseña.isEmpty()) {
            showAlert("Error", "La contraseña no puede estar vacía.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void mostrarContraseña(ActionEvent event) {
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
    private void botonVolverAction() {
        // Cierra la pantalla actual y vuelve atrás
    }

    private void actualizarEstadoBoton() {
        mostrarContraseña.setDisable(contraseña.getText().isEmpty());
    }
}
