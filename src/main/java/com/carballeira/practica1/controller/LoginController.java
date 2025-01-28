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

/**
 * Controlador de la ventana de inicio de sesión de la aplicación.
 * Gestiona la interacción del usuario con el formulario de inicio de sesión y la barra de progreso de autenticación.
 * Permite validar las credenciales del usuario y navegar a la pantalla principal.
 */
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

    /**
     * Método que se ejecuta al inicializar la ventana.
     * Configura los botones y escucha los eventos en los campos de texto para el inicio de sesión.
     */
    @FXML
    public void initialize() {
        mostrarContraseña.setDisable(true); // Deshabilita el botón de mostrar contraseña si no hay texto en la contraseña
        contraseña.textProperty().addListener((observable, oldValue, newValue) -> actualizarEstadoBoton()); // Actualiza el estado del botón al cambiar la contraseña
        idUsuario.setOnAction(event -> {
            try {
                inicioSesionAction(event);  // Llamar a inicioSesionAction y capturar excepciones
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de excepción
                showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_INICIO_SESION.getDescripcion());
            }

        });

        contraseña.setOnAction(event -> {
            try {
                inicioSesionAction(event);  // Llamar a inicioSesionAction y capturar excepciones
            } catch (IOException e) {
                e.printStackTrace(); // Manejo de excepción
                showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_INICIO_SESION.getDescripcion());
            }
        });
    }

    /**
     * Actualiza el estado del botón "Mostrar Contraseña" dependiendo de si el campo de contraseña tiene texto.
     */
    private void actualizarEstadoBoton() {
        if (contraseña.getText().isEmpty()) {
            mostrarContraseña.setDisable(true); // Deshabilita el botón si no hay texto en el campo de contraseña
        } else {
            mostrarContraseña.setDisable(false); // Habilita el botón si hay texto en el campo de contraseña
        }
    }

    /**
     * Acción que se ejecuta al hacer clic en el botón de inicio de sesión.
     * Inicia el proceso de carga y validación de las credenciales del usuario.
     * @param event El evento generado por la acción del usuario.
     * @throws IOException Si ocurre un error al cargar las pantallas.
     */
    @FXML
    private void inicioSesionAction(ActionEvent event) throws IOException {
        iniciarProcesoDeCarga();
    }

    /**
     * Inicia el proceso de carga, simulando una barra de progreso durante la verificación de credenciales.
     * Valida si el usuario y la contraseña son correctos.
     */
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
                        Platform.runLater(() -> showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_VENTANA.getDescripcion()));
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
                            Platform.runLater(() -> showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_USUARIO_NOEXISTE.getDescripcion()));
                        }
                    }

                    if (progreso >= 1.00) {
                        if (correcto > 0) {
                            // Si las credenciales son correctas, se inicia la sesión y se abre la pantalla principal
                            PantallaUtils pantallaUtils = new PantallaUtils();
                            Stage stage = new Stage();
                            FXMLLoader loader = null;
                            try {
                                loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), Constantes.TITULO_BIBLIOTECA.getDescripcion(), 400, 600);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            MenuController menuController = loader.getController();
                            menuController.initSesion(currentUser); // Inicializa la sesión del usuario
                            pantallaUtils.cerrarEstaPantalla(botonVolver); // Cierra la ventana de login
                            Platform.runLater(() -> showInfoAlert(Constantes.SESION_INICIADA.getDescripcion(), Constantes.SESION_INICIADA_MSG.getDescripcion()));
                        } else {
                            // Si las credenciales son incorrectas, muestra un mensaje de error
                            Platform.runLater(() -> showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_USUARIO_EXISTE.getDescripcion()));
                        }
                        timeline.stop();  // Detenemos la animación después de completar la carga
                    }
                })
        );

        // Configura la repetición del Timeline para que se ejecute constantemente
        timeline.setCycleCount(Timeline.INDEFINITE);  // Se ejecuta de manera indefinida
        timeline.play();
    }

    /**
     * Resetea la barra de progreso a 0.
     */
    private void resetearBarraDeProgreso() {
        progressBar.setProgress(0);  // Resetea el progreso a 0
    }

    /**
     * Acción para mostrar u ocultar la contraseña.
     * Alterna entre mostrar la contraseña como texto plano o como texto oculto.
     * @param event El evento generado por el clic en el botón "Mostrar Contraseña".
     */
    public void mostrarContraseñaAction(ActionEvent event) {
        // Alternar entre mostrar y ocultar la contraseña
        if (contador % 2 == 0) {
            // Mostrar contraseña: establecer el TextField visible y el PasswordField invisible
            contraseñaVisible.setText(contraseña.getText());
            contraseña.setVisible(false);
            contraseñaVisible.setVisible(true);
            mostrarContraseña.setText(Constantes.MOSTRAR.getDescripcion());
        } else {
            // Ocultar contraseña: volver a mostrar el PasswordField y ocultar el TextField
            contraseña.setText(contraseñaVisible.getText());
            contraseña.setVisible(true);
            contraseñaVisible.setVisible(false);
            mostrarContraseña.setText(Constantes.OCULTAR.getDescripcion());
        }
        contador++;
    }

    /**
     * Acción para cerrar la ventana de inicio de sesión y volver a la pantalla principal.
     * @param event El evento generado por el clic en el botón "Volver".
     * @throws IOException Si ocurre un error al cargar la pantalla principal.
     */
    @FXML
    private void botonVolverAction(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));  // Cierra la ventana actual
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), Constantes.TITULO_BIBLIOTECA.getDescripcion(), 400, 600);  // Muestra la pantalla principal
    }

    /**
     * Inicializa los datos de la lista de usuarios.
     * @param listaUsuarios Lista de usuarios cargada para realizar la validación.
     */
    public void initData(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
}
