package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.AlertUtils;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;

import static com.carballeira.practica1.utils.AlertUtils.showConfirmationAlert;
import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class MenuController {

    private String administrador = "";
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private final LocalDateTime fechaHoraActual = LocalDateTime.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String fechaFormateada = fechaHoraActual.format(formatter);

    @FXML
    private Button inicioSesion, crearUsuario, cerrarSesion, realizarDevolucion, realizarPrestamo, editarUsuario, borrarUsuario, botonSalir;

    @FXML
    public void initialize() {

        Usuario usuario = new Usuario("admin","admin","admin","abc123.","s");
        listaUsuarios.add(usuario);

        configurarBotonesIniciales();
    }

    private void configurarBotonesIniciales() {
        if (administrador.isEmpty()) {
            crearUsuario.setDisable(false);
            inicioSesion.setDisable(false);
            cerrarSesion.setDisable(true);
            borrarUsuario.setDisable(true);
            editarUsuario.setDisable(true);
            realizarPrestamo.setDisable(true);
            realizarDevolucion.setDisable(true);
        }
        else if(administrador.equals("s")) {
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            borrarUsuario.setDisable(false);
            editarUsuario.setDisable(false);
            realizarPrestamo.setDisable(false);
            realizarDevolucion.setDisable(false);
        }
        else if (administrador.equals("n")){
            crearUsuario.setDisable(false);
            inicioSesion.setDisable(false);
            cerrarSesion.setDisable(true);
            borrarUsuario.setDisable(false);
            editarUsuario.setDisable(false);
            realizarPrestamo.setDisable(true);
            realizarDevolucion.setDisable(true);
        }
    }

    @FXML
    private void inicioSesionAction() {

        try {
        PantallaUtils pantallaUtils = new PantallaUtils();
        pantallaUtils.cerrarEstaPantalla(inicioSesion);
        pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_LOGIN.getDescripcion(), "Iniciar Sesion", 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.");
            alert.showAndWait();
        }

    }

    @FXML
    private void crearUsuarioAction() {
        try {

            PantallaUtils pantallaUtils = new PantallaUtils();
            pantallaUtils.cerrarEstaPantalla(crearUsuario);
            pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_REGISTRO.getDescripcion(), "Crear Usuario", 550, 400);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.");
            alert.showAndWait();
        }
    }



    @FXML
    private void cerrarSesionAction() {

        showConfirmationAlert("Confirmar cierre", "¿Estás seguro de que deseas cerrar la sesión actual?", () -> {
            administrador = "";
            configurarBotonesIniciales();
            showInfoAlert("Sesión cerrada", "Sesión cerrada");
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

            showConfirmationAlert("Confirmar salida", "¿Estás seguro de que deseas salir del programa?", () -> {
            System.out.println("Saliendo del programa");
            Stage stage = (Stage) botonSalir.getScene().getWindow();
            stage.close();
        });
    }

    public void initData(Usuario usuario){

        listaUsuarios.add(usuario);

    }

}
