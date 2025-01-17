package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.AlertUtils;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class EditUserController {

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colEmail;

    @FXML
    private TableColumn<Usuario, String> colTelefono;

    @FXML
    private TableColumn<Usuario, String> colAdmin;

    @FXML
    private Button botonEditar;

    @FXML
    private Button botonVolver;

    private String nombreUsuario = "";
    private String emailUsuario = "";
    private String telefonoUsuario = "";
    private String admin = "";
    private ArrayList<Usuario> arrayUsuarios;
    private Usuario currentUser;

    /**
     * Inicializa los elementos de la tabla y su comportamiento al cargar la vista.
     * Asigna cada columna de la tabla con la propiedad correspondiente del objeto Usuario.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        tablaUsuarios.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Solo se puede seleccionar un usuario a la vez
    }

    /**
     * Muestra los usuarios en la tabla. Si no hay usuarios, se muestra un mensaje en consola.
     */
    private void mostrarUsuarios() {
        if (arrayUsuarios == null || arrayUsuarios.isEmpty()) {
            System.out.println("No hay usuarios para mostrar.");
            return;
        }

        ObservableList<Usuario> userList = FXCollections.observableArrayList(arrayUsuarios);
        tablaUsuarios.setItems(userList);
    }

    /**
     * Lógica para el botón "Editar". Permite seleccionar un usuario y modificar sus datos.
     * Si no se selecciona un usuario, se muestra un mensaje de error.
     */
    @FXML
    private void editActionPerformed() {
        Usuario selectedUser = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showErrorAlert("Error", "Por favor, selecciona un usuario para modificar.");
            return;
        }

        nombreUsuario = selectedUser.getNombre();
        emailUsuario = selectedUser.getEmail();
        telefonoUsuario = selectedUser.getTelefono();
        admin = selectedUser.getAdmin();

        // Mostrar alerta de confirmación antes de proceder con la edición
        AlertUtils.showConfirmationAlert("Confirmar modificación", "¿Estás seguro de que deseas modificar los datos del usuario: " + nombreUsuario, () -> {
            try {
                PantallaUtils pantallaUtils = new PantallaUtils();
                pantallaUtils.cerrarEstaPantalla(botonVolver); // Cierra la pantalla actual
                FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR2.getDescripcion(), "Editar Usuario Seleccionado", 600, 400);
                EditUser2Controller editUser2Controller = loader.getController();
                editUser2Controller.initData(selectedUser, arrayUsuarios, currentUser); // Pasa los datos al controlador de la pantalla de edición
            } catch (IOException e) {
                AlertUtils.showErrorAlert("Error", "No se pudo abrir la ventana de modificación: " + e.getMessage());
            }
        });
    }

    /**
     * Lógica para el botón "Volver". Cierra la pantalla actual y regresa al menú principal.
     * @param event El evento generado por la acción del usuario.
     * @throws IOException Si ocurre un error al cargar la pantalla de inicio.
     */
    @FXML
    private void VolverActionPerformed(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST)); // Cierra la ventana actual
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
        MenuController menuController = loader.getController();
        menuController.initSesion(currentUser); // Inicia el menú principal con la sesión del usuario actual
    }

    /**
     * Inicializa el controlador con la lista de usuarios y el usuario actual.
     * @param listaUsuarios Lista de usuarios a mostrar.
     * @param usuario Usuario actual que está usando la aplicación.
     */
    public void initData(ArrayList<Usuario> listaUsuarios, Usuario usuario) {
        this.arrayUsuarios = listaUsuarios;
        this.currentUser = usuario;
        mostrarUsuarios(); // Actualiza la tabla con los usuarios disponibles
    }

    /**
     * Lógica para el botón "Eliminar". Permite eliminar un usuario seleccionado.
     * Si no se selecciona un usuario, se muestra un mensaje de error.
     * Se confirma la eliminación antes de proceder.
     * @param actionEvent El evento generado por la acción del usuario (clic en el botón).
     */
    public void deleteActionPerformed(ActionEvent actionEvent) {
        Usuario selectedUser = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showErrorAlert("Error", "Por favor, selecciona un usuario para borrar.");
            return;
        }
        // Confirmar antes de eliminar
        AlertUtils.showConfirmationAlert("Confirmar borrado", "¿Estás seguro de que deseas eliminar el usuario: " + selectedUser.getNombre(), () -> {
            String emailPrevio = selectedUser.getEmail();
            String archivoUsuarios = "Usuarios.txt";
            File archivo = new File(archivoUsuarios);
            List<String> lineas = new ArrayList<>();

            // Leer el archivo y eliminar al usuario seleccionado
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    // Separar la línea por "," y verificar si no corresponde al usuario seleccionado
                    String[] datosUsuario = linea.split(",");
                    if (!datosUsuario[1].equals(emailPrevio)) { // Suponemos que el email es único
                        lineas.add(linea); // Guardar solo las líneas que no corresponden al usuario
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Ocurrió un error al leer el archivo.");
                return;
            }

            // Escribir de nuevo el archivo con las líneas restantes
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                for (String linea : lineas) {
                    writer.write(linea);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Error", "Ocurrió un error al escribir el archivo.");
                return;
            }
            arrayUsuarios.remove(selectedUser); // Eliminar el usuario de la lista en memoria
            mostrarUsuarios(); // Actualizar la tabla de usuarios
            showInfoAlert("Usuario eliminado", "Usuario eliminado correctamente");
        });
    }
}
