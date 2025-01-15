package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.carballeira.practica1.utils.AlertUtils.*;

public class MenuController {

    private String administrador = "s";
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private Usuario currentUser;

    @FXML
    private Button inicioSesion, crearUsuario, cerrarSesion, realizarDevolucion, realizarPrestamo, editarUsuario, borrarUsuario, botonSalir;

    @FXML
    public void initialize() {

        cargarUsuariosDesdeArchivo();
        configurarBotonesIniciales();
    }

    private void configurarBotonesIniciales() {
        if (administrador.isEmpty()) {
            crearUsuario.setDisable(false);
            inicioSesion.setDisable(false);
            cerrarSesion.setDisable(true);
            editarUsuario.setDisable(true);
            realizarPrestamo.setDisable(true);
            realizarDevolucion.setDisable(true);
        }
        else if(administrador.equals("s")) {
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            editarUsuario.setDisable(false);
            realizarPrestamo.setDisable(true);
            realizarDevolucion.setDisable(true);
        }
        else if (administrador.equals("n")){
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            editarUsuario.setDisable(true);
            realizarPrestamo.setDisable(false);
            realizarDevolucion.setDisable(false);
        }
    }

    @FXML
    private void inicioSesionAction() {

        try {
        PantallaUtils pantallaUtils = new PantallaUtils();
        pantallaUtils.cerrarEstaPantalla(inicioSesion);
        FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_LOGIN.getDescripcion(), "Iniciar Sesion", 600, 400);
        LoginController loginController = loader.getController();
        loginController.initData(listaUsuarios);


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
            Stage stage = new Stage();
            FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_REGISTRO.getDescripcion(), "REGISTRO",  550, 400);
            RegisterController registerController = loader.getController();
            registerController.initData(listaUsuarios);

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
        try {
            PantallaUtils pantallaUtils = new PantallaUtils();
            pantallaUtils.cerrarEstaPantalla(inicioSesion);
            FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR.getDescripcion(), "Editar Usuarios", 600, 400);
            EditUserController editUserController = loader.getController();
            editUserController.initData(listaUsuarios);


        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.");
            alert.showAndWait();
        }
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

    public void initSesion(Usuario usuario){
        currentUser = usuario;
        administrador= usuario.getAdmin();
        System.out.println(administrador);
        configurarBotonesIniciales();
    }


    private void cargarUsuariosDesdeArchivo() {
        BufferedReader reader = null;
        try {
            // Abrir el archivo Usuarios.txt para leer los datos
            reader = new BufferedReader(new FileReader("Usuarios.txt"));
            String line;

            // Leer cada línea del archivo
            while ((line = reader.readLine()) != null) {

                String[] partes = line.split(",");
                if (partes.length == 5) {
                    Usuario usuario = new Usuario(partes[0], partes[1], partes[2], partes[3], partes[4]);
                    listaUsuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir traza del error si ocurre una excepción
            showErrorAlert("Error", "No se pudo cargar el archivo de usuarios.");
        } finally {
            try {
                if (reader != null) {
                    reader.close(); // Cerrar el BufferedReader después de usarlo
                }
            } catch (IOException e) {
                e.printStackTrace(); // Imprimir traza del error si ocurre una excepción al cerrar el archivo
            }
        }
    }



}
