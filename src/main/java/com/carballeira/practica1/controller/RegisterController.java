package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;

public class RegisterController {

    @FXML
    private TextField nombre, email, telefono;

    @FXML
    private PasswordField contraseña;

    @FXML
    private TextField contraseñaVisible;

    @FXML
    private Button botonVolver, mostrarContraseña;

    private int contador = 0;  // Contador utilizado para alternar entre mostrar y ocultar contraseña
    private int contador2 = 0; // Contador utilizado para verificar si el usuario ya existe
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>(); // Lista de usuarios registrados

    @FXML
    private void initialize() {
        // Deshabilitar el botón de mostrar contraseña inicialmente
        mostrarContraseña.setDisable(true);

        // Listener para actualizar el estado del botón al escribir la contraseña
        contraseña.textProperty().addListener((observable, oldValue, newValue) -> actualizarEstadoBoton());

        // Definir las acciones cuando se presionan las teclas en los campos
        nombre.setOnAction(this::crearUsuario);
        email.setOnAction(this::crearUsuario);
        telefono.setOnAction(this::crearUsuario);
        contraseña.setOnAction(this::crearUsuario);

        // Acción de tecla ESC para volver
        botonVolver.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                botonVolver.fire();  // Ejecuta la acción de volver si se presiona ESC
            }
        });

        // Acción del botón Volver
        botonVolver.setOnAction(event -> {
            try {
                botonVolverAction();  // Llama al método para cerrar la pantalla actual y volver a la inicial
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void crearUsuario(ActionEvent event) {
        // Obtener los valores ingresados por el usuario
        String nombre1 = nombre.getText();
        String email1 = email.getText();
        String telefono1 = telefono.getText();
        String contraseña1 = contraseña.getText();
        contador2 = 0;  // Reiniciar contador de verificación de existencia del usuario
        Usuario usuario = new Usuario(nombre1, email1, telefono1, contraseña1, "n");

        // Verificar si el usuario ya existe en la lista
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(usuario.getEmail())) {
                contador2++; // Incrementar el contador si el usuario ya existe
            }
        }

        // Validar si los campos son correctos
        if (usuario.validarCampos(nombre1, email1, telefono1, contraseña1)) {

            // Si el contador es mayor a 0, significa que el usuario ya existe
            if (contador2 > 0) {
                showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_USUARIO_EXISTE.getDescripcion());
                contador2 = 0; // Reiniciar el contador
            } else {
                // Si el usuario no existe, proceder con la creación del usuario
                try {
                    // Crear una nueva ventana para el CAPTCHA
                    PantallaUtils pantallaUtils = new PantallaUtils();
                    Stage stage = new Stage();
                    FXMLLoader loader = pantallaUtils.showEstaPantalla(
                            stage,
                            Constantes.PAGINA_CAPTCHA.getDescripcion(),
                            Constantes.TITULO_CAPTCHA.getDescripcion(),
                            400,
                            600
                    );
                    CaptchaController captchaController = loader.getController();
                    captchaController.initData(usuario);  // Enviar datos del usuario al controlador CAPTCHA
                    pantallaUtils.cerrarEstaPantalla(botonVolver);  // Cerrar la pantalla actual
                } catch (IOException e) {
                    e.printStackTrace();  // Imprimir la traza del error
                    Alert alert = new Alert(Alert.AlertType.ERROR, Constantes.ERROR_VENTANA_CAPTCHA.getDescripcion());
                    alert.showAndWait();  // Mostrar alerta de error si no se puede abrir la ventana CAPTCHA
                }
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
            mostrarContraseña.setText(Constantes.OCULTAR.getDescripcion());  // Cambiar el texto del botón
        } else {
            // Ocultar contraseña: volver a mostrar el PasswordField y ocultar el TextField
            contraseña.setText(contraseñaVisible.getText());
            contraseña.setVisible(true);
            contraseñaVisible.setVisible(false);
            mostrarContraseña.setText(Constantes.MOSTRAR.getDescripcion());  // Cambiar el texto del botón
        }
        contador++;  // Incrementar el contador para alternar el estado
    }

    @FXML
    private void botonVolverAction() throws IOException {
        // Cerrar la pantalla actual y abrir la pantalla inicial
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        pantallaUtils.showEstaPantalla(
                stage,
                Constantes.PAGINA_INICIAL.getDescripcion(),
                Constantes.TITULO_BIBLIOTECA.getDescripcion(),
                400,
                600
        );
    }

    private void actualizarEstadoBoton() {
        // Habilitar o deshabilitar el botón de mostrar contraseña según si el campo de contraseña no está vacío
        mostrarContraseña.setDisable(contraseña.getText().isEmpty());
    }

    public void initData(ArrayList<Usuario> listaUsuarios) {
        // Inicializar la lista de usuarios
        this.listaUsuarios = listaUsuarios;
    }
}
