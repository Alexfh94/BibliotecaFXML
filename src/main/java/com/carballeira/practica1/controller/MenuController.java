package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Libro;
import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.carballeira.practica1.utils.AlertUtils.*;

public class MenuController {

    private String administrador = "";
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Libro> listaLibros = new ArrayList<>();
    private Usuario currentUser;
    private final Usuario usuario = new Usuario();
    private final Libro libro = new Libro();

    @FXML
    private Button inicioSesion, crearUsuario, cerrarSesion, realizarDevolucion, editarUsuario, botonSalir;

    @FXML
    private Label usuarioActualLabel;

    @FXML
    public void initialize() {
        cargarUsuariosDesdeBBDD();
        cargarLibrosDesdeBBDD();
        configurarBotonesIniciales();
    }

    private void configurarBotonesIniciales() {
        if (administrador.isEmpty()) {
            crearUsuario.setDisable(false);
            inicioSesion.setDisable(false);
            cerrarSesion.setDisable(true);
            editarUsuario.setDisable(true);
            realizarDevolucion.setDisable(true);
        } else if (administrador.equals(Constantes.ES_ADMIN.getDescripcion())) {
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            editarUsuario.setDisable(false);
            realizarDevolucion.setDisable(true);
        } else if (administrador.equals(Constantes.NO_ADMIN.getDescripcion())) {
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            editarUsuario.setDisable(true);
            realizarDevolucion.setDisable(false);
        }
    }

    private Object abrirNuevaVentana(Button button, String fxmlPath, String title, int width, int height) throws IOException {
        PantallaUtils pantallaUtils = new PantallaUtils();
        pantallaUtils.cerrarEstaPantalla(button);
        FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), fxmlPath, title, width, height);
        return loader.getController();
    }

    @FXML
    private void inicioSesionAction() {
        try {
            LoginController loginController = (LoginController) abrirNuevaVentana(
                    inicioSesion,
                    Constantes.PAGINA_LOGIN.getDescripcion(),
                    Constantes.TITULO_LOGIN.getDescripcion(),
                    600,
                    400
            );
            loginController.initData(listaUsuarios);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(Constantes.ERROR_VENTANA.getDescripcion(), Constantes.ERROR_INICIO_SESION.getDescripcion());
        }
    }

    @FXML
    private void crearUsuarioAction() {
        try {
            RegisterController registerController = (RegisterController) abrirNuevaVentana(
                    crearUsuario,
                    Constantes.PAGINA_REGISTRO.getDescripcion(),
                    Constantes.TITULO_REGISTRO.getDescripcion(),
                    550,
                    400
            );
            registerController.initData(listaUsuarios);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(Constantes.ERROR_VENTANA.getDescripcion(), Constantes.ERROR_REGISTRO.getDescripcion());
        }
    }

    @FXML
    private void cerrarSesionAction() {
        showConfirmationAlert(Constantes.CONFIRMAR_CIERRE.getDescripcion(), Constantes.CONFIRMAR_CIERRE_MSG.getDescripcion(), () -> {
            administrador = "";
            configurarBotonesIniciales();
            usuarioActualLabel.setText(Constantes.USUARIO_NO_SESION.getDescripcion());
            showInfoAlert(Constantes.SESION_CERRADA.getDescripcion(), Constantes.SESION_CERRADA_MSG.getDescripcion());
        });
    }

    @FXML
    private void realizarDevolucionAction() throws IOException {
        PrestamoController prestamoController = (PrestamoController) abrirNuevaVentana(
                inicioSesion,
                Constantes.PAGINA_PRESTAMO.getDescripcion(),
                Constantes.TITULO_PRESTAMO.getDescripcion(),
                1200,
                600
        );
        prestamoController.initData(currentUser, listaLibros);
    }

    @FXML
    private void editarUsuarioAction() {
        try {
            EditUserController editUserController = (EditUserController) abrirNuevaVentana(
                    inicioSesion,
                    Constantes.PAGINA_MODIFICAR.getDescripcion(),
                    Constantes.TITULO_EDITAR_USUARIO.getDescripcion(),
                    600,
                    400
            );
            editUserController.initData(listaUsuarios, currentUser);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(Constantes.ERROR_VENTANA.getDescripcion(), Constantes.ERROR_EDITAR_USUARIO.getDescripcion());
        }
    }

    @FXML
    private void botonSalirAction() {
        showConfirmationAlert(Constantes.CONFIRMAR_SALIDA.getDescripcion(), Constantes.CONFIRMAR_SALIDA_MSG.getDescripcion(), () -> {
            Stage stage = (Stage) botonSalir.getScene().getWindow();
            stage.close();
        });
    }

    public void initData(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    public void initSesion(Usuario usuario) {
        currentUser = usuario;
        administrador = usuario.getAdmin();
        configurarBotonesIniciales();
        usuarioActualLabel.setText(Constantes.USUARIO_ACTUAL.getDescripcion() + currentUser.getNombre());
    }

    private void cargarUsuariosDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Constantes.ARCHIVO_USUARIOS.getDescripcion()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                if (partes.length == 5) {
                    Usuario usuario = new Usuario(partes[0], partes[1], partes[2], partes[3], partes[4]);
                    listaUsuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(Constantes.ERROR_ARCHIVO.getDescripcion(), Constantes.ERROR_CARGAR_USUARIOS.getDescripcion());
        }
    }

    private void cargarUsuariosDesdeBBDD(){
        listaUsuarios =  usuario.obtenerTodosUsuarios();
    }

    private void cargarLibrosDesdeBBDD(){
        listaLibros =  libro.obtenerTodosLibros();
    }


    private void cargarLibrosDesdeArchivo() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(Constantes.ARCHIVO_LIBROS.getDescripcion()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                if (partes.length == 4) {
                    Boolean disponible = partes[3].equals("true");
                    Libro libro = new Libro(partes[0], partes[1], partes[2], disponible);
                    listaLibros.add(libro);
                }
                if (partes.length == 7) {
                    Boolean disponible = partes[3].equals("true");
                    Libro libro = new Libro(partes[0], partes[1], partes[2], disponible, partes[4], partes[5], partes[6]);
                    listaLibros.add(libro);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_CARGAR_LIBROS.getDescripcion());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
