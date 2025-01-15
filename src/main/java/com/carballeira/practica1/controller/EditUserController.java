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


    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));

        tablaUsuarios.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }


    private void mostrarUsuarios() {
        if (arrayUsuarios == null || arrayUsuarios.isEmpty()) {
            System.out.println("No hay usuarios para mostrar.");
            return;
        }

        ObservableList<Usuario> userList = FXCollections.observableArrayList(arrayUsuarios);
        tablaUsuarios.setItems(userList);
    }



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


        AlertUtils.showConfirmationAlert("Confirmar modificación", "¿Estás seguro de que deseas modificar los datos del usuario: " + nombreUsuario, () -> {
            try {
                PantallaUtils pantallaUtils = new PantallaUtils();
                pantallaUtils.cerrarEstaPantalla(botonVolver);
                FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR2.getDescripcion(), "Editar Usuario Seleccionado", 600, 400);
                EditUser2Controller editUser2Controller = loader.getController();
                editUser2Controller.initData(selectedUser, arrayUsuarios);

            } catch (IOException e) {
                AlertUtils.showErrorAlert("Error", "No se pudo abrir la ventana de modificación: " + e.getMessage());
            }
        });
    }

    @FXML
    private void VolverActionPerformed(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
    }


    public void initData(ArrayList<Usuario> listaUsuarios) {
        this.arrayUsuarios = listaUsuarios;
        mostrarUsuarios(); // Actualizar la lista una vez que los datos estén disponibles

    }

    public void deleteActionPerformed(ActionEvent actionEvent) {
        Usuario selectedUser = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showErrorAlert("Error", "Por favor, selecciona un usuario para borrar.");
            return;
        }
        AlertUtils.showConfirmationAlert("Confirmar borrado", "¿Estás seguro de que deseas eliminar el usuario: " + nombreUsuario, () -> {
            String emailPrevio = selectedUser.getEmail();
            String archivoUsuarios = "Usuarios.txt";
            File archivo = new File(archivoUsuarios);
            List<String> lineas = new ArrayList<>();


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
            arrayUsuarios.remove(selectedUser);
            mostrarUsuarios();
            showInfoAlert("Usuario eliminado", "Usuario eliminado correctamente");

        });
        }
}

