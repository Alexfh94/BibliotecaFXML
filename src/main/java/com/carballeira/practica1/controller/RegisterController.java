package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;

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

        Usuario usuario = new Usuario(nombre1,email1,telefono1,contraseña1,"n");

        if (usuario.validarCampos(nombre1, email1, telefono1, contraseña1)) {


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/carballeira/practica1/captcha-view.fxml"));
                Parent root = loader.load();  // Cargar la vista FXML
                CaptchaController captchaController = loader.getController();  // Obtener el controlador automáticamente
                captchaController.initData(nombre1, email1, telefono1, contraseña1);

                // Mostrar la nueva ventana (captcha)
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Verificación CAPTCHA");
                stage.show();

                // Cerrar la ventana actual de registro
                Stage currentStage = (Stage) crearUsuario.getScene().getWindow();
                currentStage.hide();

            } catch (IOException e) {
                e.printStackTrace(); // Imprimir la traza del error
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
                alert.showAndWait();
            }
        }
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
        // Disparar el evento de cierre como si el usuario hubiera presionado la "X"
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void actualizarEstadoBoton() {
        mostrarContraseña.setDisable(contraseña.getText().isEmpty());
    }
}
