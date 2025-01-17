package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Libro;
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

/**
 * Controlador principal del menú de la aplicación, maneja las interacciones del usuario con la interfaz de usuario.
 * Permite gestionar la sesión del usuario, registrar usuarios, realizar préstamos, devoluciones, y editar usuarios.
 */
public class MenuController {

    private String administrador = "";
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private ArrayList<Libro> listaLibros = new ArrayList<>();
    private Usuario currentUser;

    @FXML
    private Button inicioSesion, crearUsuario, cerrarSesion, realizarDevolucion, editarUsuario, botonSalir;

    /**
     * Método que se ejecuta cuando se inicializa la ventana.
     * Carga los usuarios y libros desde los archivos correspondientes
     * y configura los botones de la interfaz.
     */
    @FXML
    public void initialize() {
        cargarUsuariosDesdeArchivo();
        cargarLibrosDesdeArchivo();
        configurarBotonesIniciales();
    }

    /**
     * Configura el estado inicial de los botones según el tipo de usuario (administrador o usuario regular).
     * Desactiva o activa botones dependiendo de si el usuario es administrador, usuario regular, o si no ha iniciado sesión.
     */
    private void configurarBotonesIniciales() {
        if (administrador.isEmpty()) {
            crearUsuario.setDisable(false);
            inicioSesion.setDisable(false);
            cerrarSesion.setDisable(true);
            editarUsuario.setDisable(true);
            realizarDevolucion.setDisable(true);
        } else if (administrador.equals("s")) {
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            editarUsuario.setDisable(false);
            realizarDevolucion.setDisable(true);
        } else if (administrador.equals("n")) {
            crearUsuario.setDisable(true);
            inicioSesion.setDisable(true);
            cerrarSesion.setDisable(false);
            editarUsuario.setDisable(true);
            realizarDevolucion.setDisable(false);
        }
    }

    /**
     * Método reutilizable para abrir nuevas ventanas.
     * @param button Botón que cierra la ventana actual.
     * @param fxmlPath Ruta al archivo FXML de la nueva ventana.
     * @param title Título de la nueva ventana.
     * @param width Ancho de la nueva ventana.
     * @param height Alto de la nueva ventana.
     * @return El controlador de la nueva ventana.
     * @throws IOException Si ocurre un error al cargar la ventana.
     */
    private Object abrirNuevaVentana(Button button, String fxmlPath, String title, int width, int height) throws IOException {
        PantallaUtils pantallaUtils = new PantallaUtils();
        pantallaUtils.cerrarEstaPantalla(button);
        FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), fxmlPath, title, width, height);
        return loader.getController();
    }

    /**
     * Abre la ventana de inicio de sesión.
     */
    @FXML
    private void inicioSesionAction() {
        try {
            LoginController loginController = (LoginController) abrirNuevaVentana(
                    inicioSesion,
                    Constantes.PAGINA_LOGIN.getDescripcion(),
                    "Iniciar Sesion",
                    600,
                    400
            );
            loginController.initData(listaUsuarios);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.").showAndWait();
        }
    }

    /**
     * Abre la ventana para crear un nuevo usuario.
     */
    @FXML
    private void crearUsuarioAction() {
        try {
            RegisterController registerController = (RegisterController) abrirNuevaVentana(
                    crearUsuario,
                    Constantes.PAGINA_REGISTRO.getDescripcion(),
                    "REGISTRO",
                    550,
                    400
            );
            registerController.initData(listaUsuarios);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.").showAndWait();
        }
    }

    /**
     * Cierra la sesión actual.
     */
    @FXML
    private void cerrarSesionAction() {
        showConfirmationAlert("Confirmar cierre", "¿Estás seguro de que deseas cerrar la sesión actual?", () -> {
            administrador = "";
            configurarBotonesIniciales();
            showInfoAlert("Sesión cerrada", "Sesión cerrada");
        });
    }

    /**
     * Abre la pantalla para realizar préstamos y devoluciones de libros.
     */
    @FXML
    private void realizarDevolucionAction() throws IOException {
        PrestamoController prestamoController = (PrestamoController) abrirNuevaVentana(
                inicioSesion,
                Constantes.PAGINA_PRESTAMO.getDescripcion(),
                "Realizar prestamos y devoluciones",
                1200,
                600
        );
        prestamoController.initData(currentUser, listaLibros);
    }

    /**
     * Abre la pantalla para editar un usuario.
     */
    @FXML
    private void editarUsuarioAction() {
        try {
            EditUserController editUserController = (EditUserController) abrirNuevaVentana(
                    inicioSesion,
                    Constantes.PAGINA_MODIFICAR.getDescripcion(),
                    "Editar Usuarios",
                    600,
                    400
            );
            editUserController.initData(listaUsuarios, currentUser);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana.").showAndWait();
        }
    }

    /**
     * Método que se ejecuta cuando el usuario desea salir de la aplicación.
     */
    @FXML
    private void botonSalirAction() {
        showConfirmationAlert("Confirmar salida", "¿Estás seguro de que deseas salir del programa?", () -> {
            Stage stage = (Stage) botonSalir.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Inicializa los datos de un usuario que se agrega a la lista de usuarios.
     * @param usuario El usuario que se añadirá a la lista de usuarios.
     */
    public void initData(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    /**
     * Inicializa la sesión con los datos de un usuario.
     * Configura la información del usuario actual y actualiza la interfaz de acuerdo a su rol.
     * @param usuario El usuario que ha iniciado sesión.
     */
    public void initSesion(Usuario usuario) {
        currentUser = usuario;
        administrador = usuario.getAdmin();
        configurarBotonesIniciales();
    }

    /**
     * Carga los usuarios desde el archivo `Usuarios.txt` y los agrega a la lista de usuarios.
     */
    private void cargarUsuariosDesdeArchivo() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("Usuarios.txt"));
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
            showErrorAlert("Error", "No se pudo cargar el archivo de usuarios.");
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

    /**
     * Carga los libros desde el archivo `Libros.txt` y los agrega a la lista de libros.
     */
    private void cargarLibrosDesdeArchivo() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("Libros.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(",");
                if (partes.length == 4) {
                    Boolean disponible = partes[3].equals("true");
                    Libro libro = new Libro(partes[0], partes[1], partes[2], disponible);
                    listaLibros.add(libro);
                }
                if (partes.length == 6) {
                    Boolean disponible = partes[3].equals("true");
                    Libro libro = new Libro(partes[0], partes[1], partes[2], disponible, partes[4], partes[5]);
                    listaLibros.add(libro);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "No se pudo cargar el archivo de libros.");
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
