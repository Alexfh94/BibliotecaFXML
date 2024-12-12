package com.carballeira.practica1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.carballeira.practica1.utils.AlertUtils.showConfirmationAlert;
import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class MenuController {

    private String administrador = "";
    private int idUsuario = 0;
    private String email = "";
    private String telefono = "";
    private String nombre = "";
    private final LocalDateTime fechaHoraActual = LocalDateTime.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String fechaFormateada = fechaHoraActual.format(formatter);

    @FXML
    private Button inicioSesion, crearUsuario, cerrarSesion, realizarDevolucion, realizarPrestamo, buscarUsuario, editarUsuario, borrarUsuario, botonSalir;

    @FXML
    public void initialize() {
        configurarBotonesIniciales();
    }

    private void configurarBotonesIniciales() {
        if (administrador.isEmpty()) {
            crearUsuario.setDisable(false);
            inicioSesion.setDisable(false);
            cerrarSesion.setDisable(true);
            borrarUsuario.setDisable(true);
            buscarUsuario.setDisable(true);
            editarUsuario.setDisable(true);
            realizarPrestamo.setDisable(true);
            realizarDevolucion.setDisable(true);
        }
    }

    @FXML
    private void inicioSesionAction() {
        // Lógica para iniciar sesión
        System.out.println("Iniciar sesión clickeado");
        // Simular cambio a otra ventana o lógica específica
    }

    @FXML
    private void crearUsuarioAction() {
        try {
            // Obtener el escenario actual (ventana del menú)
            Stage currentStage = (Stage) crearUsuario.getScene().getWindow();
            // Ocultar la ventana del menú
            currentStage.hide();

            // Cargar el archivo FXML de la ventana de creación de usuario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/carballeira/practica1/register-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Crear un nuevo escenario (ventana de creación de usuario)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Crear Usuario");

            // Mostrar la ventana de creación de usuario
            stage.show();

            // Establecer un listener para cerrar la ventana de creación de usuario y mostrar nuevamente la ventana del menú
            stage.setOnCloseRequest(e -> currentStage.show());

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.");
            alert.showAndWait();
        }
    }


    @FXML
    private void cerrarSesionAction() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas cerrar la sesión actual?", ButtonType.YES, ButtonType.NO);
        confirmacion.setHeaderText(null);
        confirmacion.setTitle("Confirmar cierre");
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                administrador = "";
                configurarBotonesIniciales();
                showInfoAlert("Sesion cerrada","Sesion cerrada");
            }
        });
    }

    @FXML
    private void realizarDevolucionAction() {
        System.out.println("Realizar devolución clickeado");
        // Lógica para realizar devolución
    }

    @FXML
    private void realizarPrestamoAction() {
        System.out.println("Realizar préstamo clickeado");
        // Lógica para realizar préstamo
    }

    @FXML
    private void buscarUsuarioAction() {
        System.out.println("Buscar usuario clickeado");
        // Lógica para buscar usuario
    }

    @FXML
    private void editarUsuarioAction() {
        System.out.println("Editar usuario clickeado");
        // Lógica para modificar usuario
    }

    @FXML
    private void borrarUsuarioAction() {
        System.out.println("Borrar usuario clickeado");
        // Lógica para borrar usuario
    }

    @FXML
    private void botonSalirAction() {
        // Usamos AlertUtils para mostrar la alerta de confirmación
            showConfirmationAlert("Confirmar salida", "¿Estás seguro de que deseas salir del programa?", () -> {
            System.out.println("Saliendo del programa");
            Stage stage = (Stage) botonSalir.getScene().getWindow();
            stage.close();
        });
    }

}
