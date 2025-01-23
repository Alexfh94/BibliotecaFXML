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
            System.out.println(Constantes.NO_HAY_USUARIOS.getDescripcion());
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
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_EDITAR_USUARIO_SELECCIONADO.getDescripcion());
            return;
        }

        nombreUsuario = selectedUser.getNombre();
        emailUsuario = selectedUser.getEmail();
        telefonoUsuario = selectedUser.getTelefono();
        admin = selectedUser.getAdmin();

        // Mostrar alerta de confirmación antes de proceder con la edición
        AlertUtils.showConfirmationAlert(Constantes.CONFIRMAR_MODIFICACION.getDescripcion(), Constantes.CONFIRMAR_MODIFICACION_MSG.getDescripcion() + nombreUsuario, () -> {
            try {
                PantallaUtils pantallaUtils = new PantallaUtils();
                pantallaUtils.cerrarEstaPantalla(botonVolver); // Cierra la pantalla actual
                FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR2.getDescripcion(), Constantes.TITULO_EDITAR_USUARIO.getDescripcion(), 600, 400);
                EditUser2Controller editUser2Controller = loader.getController();
                editUser2Controller.initData(selectedUser, arrayUsuarios, currentUser); // Pasa los datos al controlador de la pantalla de edición
            } catch (IOException e) {
                AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_ABRIR_MODIFICACION.getDescripcion() + e.getMessage());
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
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), Constantes.TITULO_BIBLIOTECA.getDescripcion(), 400, 600);
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
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_ELIMINAR_USUARIO.getDescripcion());
            return;
        }
        // Confirmar antes de eliminar
        AlertUtils.showConfirmationAlert(Constantes.CONFIRMAR_BORRADO.getDescripcion(), Constantes.CONFIRMAR_BORRADO_MSG.getDescripcion() + selectedUser.getNombre(), () -> {
            if (borrarUsuarioArchivo(selectedUser)) return;
            arrayUsuarios.remove(selectedUser); // Eliminar el usuario de la lista en memoria
            borrarUsuarioBBDD(selectedUser);
            mostrarUsuarios(); // Actualizar la tabla de usuarios
            showInfoAlert(Constantes.USUARIO_ELIMINADO.getDescripcion(), Constantes.USUARIO_ELIMINADO_MSG.getDescripcion());
        });
    }

    private static boolean borrarUsuarioArchivo(Usuario selectedUser) {
        String emailPrevio = selectedUser.getEmail();
        String archivoUsuarios = Constantes.ARCHIVO_USUARIOS.getDescripcion();
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
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_LECTURA_ARCHIVO.getDescripcion());
            return true;
        }

        // Escribir de nuevo el archivo con las líneas restantes
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.ERROR_ESCRITURA_ARCHIVO.getDescripcion());
            return true;
        }
        return false;
    }

    private static boolean borrarUsuarioBBDD(Usuario selectedUser){
        selectedUser.eliminarUsuario(selectedUser.getEmail());
        return false;
    }
}
