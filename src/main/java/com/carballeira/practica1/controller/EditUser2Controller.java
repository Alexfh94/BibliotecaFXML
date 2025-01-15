package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class EditUser2Controller {

    private Usuario usuario;
    private ArrayList<Usuario> listaUsuarios;

    // Campos de la vista FXML
    @FXML
    private TextField nombre, email, telefono;

    @FXML
    private CheckBox adminCB;

    @FXML
    private Button botonConfirmar;

    @FXML
    private Button botonVolver;

    // Método de inicialización, se llama automáticamente después de cargar el FXML
    public void initialize() {
        if (usuario != null) {
            // Si el usuario no es nulo, se cargan sus datos en los campos correspondientes
            nombre.setText(usuario.getNombre());
            email.setText(usuario.getEmail());
            telefono.setText(usuario.getTelefono());

            // Asignar el estado del CheckBox de admin basado en el valor de usuario
            if ("s".equals(usuario.getAdmin())) {
                adminCB.setSelected(true);
            } else {
                adminCB.setSelected(false);
            }
        }
    }

    // Método para inicializar el controlador con un objeto Usuario
    public void initData(Usuario usuario,ArrayList<Usuario> listaUsuarios) {
        this.usuario = usuario;
        this.listaUsuarios=listaUsuarios;
        initialize();
    }

    // Lógica para el botón "Editar Usuario"
    public void editarUsuario(ActionEvent actionEvent) throws IOException {
        String emailPrevio = usuario.getEmail();
        usuario.setNombre(nombre.getText());
        usuario.setEmail(email.getText());
        usuario.setTelefono(telefono.getText());
        usuario.setAdmin(adminCB.isSelected() ? "s" : "n");

        // Ruta del archivo donde están los datos de los usuarios
        String archivoUsuarios = "Usuarios.txt";

        // Leer el archivo y almacenar todas las líneas
        File archivo = new File(archivoUsuarios);
        List<String> lineas = new ArrayList<>();
        boolean usuarioEncontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Separar la línea por ";"
                String[] datosUsuario = linea.split(",");

                // Verificar si la línea corresponde al usuario que estamos editando
                if (datosUsuario[1].equals(emailPrevio)) { // Suponemos que el email es único
                    // Actualizar los datos de la línea con los nuevos datos
                    datosUsuario[0] = usuario.getNombre();
                    datosUsuario[1] = usuario.getEmail();
                    datosUsuario[2] = usuario.getTelefono();
                    datosUsuario[3] = usuario.getAdmin();

                    // Reconstruir la línea con los nuevos datos
                    String nuevaLinea = String.join(",", datosUsuario);
                    lineas.add(nuevaLinea);
                    usuarioEncontrado = true;
                } else {
                    // Si no es el usuario a editar, simplemente agregar la línea original
                    lineas.add(linea);
                }
            }

            // Si no se encuentra el usuario, podemos agregar un mensaje o lanzar una excepción
            if (!usuarioEncontrado) {
                System.out.println("Usuario no encontrado para editar.");
                return; // Terminar si no encontramos al usuario
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sobrescribir el archivo con las líneas actualizadas
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                writer.write(linea);
                writer.newLine();
            }
            PantallaUtils pantallaUtils = new PantallaUtils();
            pantallaUtils.cerrarEstaPantalla(botonVolver);
            FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR.getDescripcion(), "Editar Usuarios", 600, 400);
            EditUserController editUserController = loader.getController();
            editUserController.initData(listaUsuarios);
            showInfoAlert("Datos modificados", "Usuario actualizado correctamente");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    // Lógica para el botón "Volver"
    public void botonVolverActionPerformed(ActionEvent actionEvent) throws IOException {
        PantallaUtils pantallaUtils = new PantallaUtils();
        pantallaUtils.cerrarEstaPantalla(botonVolver);
        FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR.getDescripcion(), "Editar Usuarios", 600, 400);
        EditUserController editUserController = loader.getController();
        editUserController.initData(listaUsuarios);
    }
}
