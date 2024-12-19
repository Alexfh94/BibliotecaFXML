package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;
import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;

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

    private int contador;
    private int correcto = 0;
    private ArrayList<Usuario> listaUsuarios;
    private Usuario currentUser;
    private Timeline timeline = new Timeline();

    @FXML
    public void initialize() {
        mostrarContraseña.setDisable(true);
        contraseña.textProperty().addListener((observable, oldValue, newValue) -> actualizarEstadoBoton());
        idUsuario.setOnAction(event -> {
            try {
                inicioSesionAction(event);  // Llamar a inicioSesionAction y capturar excepciones
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de excepción
                showErrorAlert("Error", "Ocurrió un error durante el inicio de sesión.");
            }
        });

        contraseña.setOnAction(event -> {
            try {
                inicioSesionAction(event);  // Llamar a inicioSesionAction y capturar excepciones
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de excepción
                showErrorAlert("Error", "Ocurrió un error durante el inicio de sesión.");
            }
        });
    }

    private void actualizarEstadoBoton() {
        if (contraseña.getText().isEmpty()) {
            mostrarContraseña.setDisable(true);
        } else {
            mostrarContraseña.setDisable(false);
        }
    }

    @FXML
    private void inicioSesionAction(ActionEvent event) throws IOException {

        iniciarProcesoDeCarga();
    }

    private void iniciarProcesoDeCarga() {
        // Establece el progreso inicial de la barra
        progressBar.setProgress(0);

        // Crea un Timeline para simular la barra de progreso de manera constante
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), e -> {
                    // Incrementa el progreso de la barra a una velocidad constante
                    double progreso = progressBar.getProgress() + 0.01;
                    progressBar.setProgress(progreso);

                    // Lógica de verificación: mostramos alertas en momentos específicos
                    if (progreso > 0.10 && progreso <= 0.20 && (idUsuario.getText().isEmpty() || contraseña.getText().isEmpty())) {
                        // Detener la animación, resetear progreso y mostrar alerta de error
                        timeline.stop();
                        resetearBarraDeProgreso();
                        Platform.runLater(() -> showErrorAlert("Error", "Los campos no pueden estar vacíos."));
                    }

                    if (progreso > 0.40 && progreso <= 0.50) {
                        boolean usuarioEncontrado = false;
                        for (int i = 0; i < listaUsuarios.size(); i++) {
                            if (listaUsuarios.get(i).getNombre().equals(idUsuario.getText()) &&
                                    listaUsuarios.get(i).getContraseña().equals(contraseña.getText())) {
                                correcto++;
                                currentUser = listaUsuarios.get(i);
                                usuarioEncontrado = true;
                            }
                        }

                        if (!usuarioEncontrado) {
                            // Detener la animación, resetear progreso y mostrar alerta de error
                            timeline.stop();
                            resetearBarraDeProgreso();
                            Platform.runLater(() -> showErrorAlert("Error", "Usuario no encontrado o contraseña incorrecta."));
                        }
                    }

                    if (progreso >= 1.00) {
                        if (correcto > 0) {
                            PantallaUtils pantallaUtils = new PantallaUtils();
                            Stage stage = new Stage();
                            FXMLLoader loader = null;
                            try {
                                loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            MenuController menuController = loader.getController();
                            menuController.initSesion(currentUser);
                            pantallaUtils.cerrarEstaPantalla(botonVolver);
                            Platform.runLater(() -> showInfoAlert("Sesión iniciada", "Sesión iniciada para el usuario: " + currentUser.getNombre()));
                        } else {
                            Platform.runLater(() -> showErrorAlert("Error", "Usuario o contraseña incorrectos."));
                        }
                        timeline.stop();  // Detenemos la animación después de completar la carga
                    }
                })
        );

        // Configura la repetición del Timeline para que se ejecute constantemente
        timeline.setCycleCount(Timeline.INDEFINITE);  // Se ejecuta de manera indefinida
        timeline.play();
    }


    private void resetearBarraDeProgreso() {
        progressBar.setProgress(0);  // Resetea el progreso a 0
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
    private void botonVolverAction(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
    }

    public void initData(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
}
