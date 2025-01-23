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

/**
 * Controlador para editar los detalles de un usuario existente.
 * Permite actualizar el nombre, correo electrónico, teléfono y estado de administrador de un usuario.
 */
public class EditUser2Controller {

    private Usuario usuario;
    private ArrayList<Usuario> listaUsuarios;
    private Usuario currentUser;

    // Campos de la vista FXML
    @FXML
    private TextField nombre, email, telefono;

    @FXML
    private CheckBox adminCB;

    @FXML
    private Button botonConfirmar;

    @FXML
    private Button botonVolver;

    /**
     * Método de inicialización que se llama automáticamente después de cargar el FXML.
     * Se usa para cargar los datos actuales del usuario en los campos correspondientes.
     */
    public void initialize() {
        if (usuario != null) {
            // Si el usuario no es nulo, se cargan sus datos en los campos correspondientes
            nombre.setText(usuario.getNombre());
            email.setText(usuario.getEmail());
            telefono.setText(usuario.getTelefono());

            // Asignar el estado del CheckBox de admin basado en el valor de usuario
            if (Constantes.ES_ADMIN.getDescripcion().equals(usuario.getAdmin())) {
                adminCB.setSelected(true);
            } else {
                adminCB.setSelected(false);
            }
        }
    }

    /**
     * Inicializa el controlador con un objeto Usuario y la lista de usuarios.
     * @param usuario El usuario que se va a editar.
     * @param listaUsuarios Lista de todos los usuarios.
     * @param currentUser El usuario que está actualmente autenticado en la aplicación.
     */
    public void initData(Usuario usuario, ArrayList<Usuario> listaUsuarios, Usuario currentUser) {
        this.usuario = usuario;
        this.listaUsuarios = listaUsuarios;
        this.currentUser = currentUser;
        initialize();
    }

    /**
     * Lógica para el botón "Editar Usuario".
     * Permite actualizar la información del usuario y guardar los cambios en el archivo de usuarios.
     * @param actionEvent El evento generado por la acción del usuario (clic en el botón).
     * @throws IOException Si ocurre un error al leer o escribir en el archivo de usuarios.
     */
    public void editarUsuario(ActionEvent actionEvent) throws IOException {
        String emailPrevio = usuario.getEmail();
        usuario.setNombre(nombre.getText());
        usuario.setEmail(email.getText());
        usuario.setTelefono(telefono.getText());
        usuario.setAdmin(adminCB.isSelected() ? Constantes.ES_ADMIN.getDescripcion() : Constantes.NO_ADMIN.getDescripcion());

        // Ruta del archivo donde están los datos de los usuarios
        String archivoUsuarios = Constantes.ARCHIVO_USUARIOS.getDescripcion();

        // Leer el archivo y almacenar todas las líneas
        File archivo = new File(archivoUsuarios);
        List<String> lineas = new ArrayList<>();
        boolean usuarioEncontrado = false;

        if (leerArchivo(archivo, emailPrevio, lineas, usuarioEncontrado)) return; // Terminar si no encontramos al usuario

        // Sobrescribir el archivo con las líneas actualizadas
        //escribirArchivo(archivo, lineas);
        escribirBBDD(usuario);
    }

    private boolean leerArchivo(File archivo, String emailPrevio, List<String> lineas, boolean usuarioEncontrado) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Separar la línea por ","
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
                System.out.println(Constantes.ERROR_EDITAR_USUARIO.getDescripcion());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void escribirArchivo(File archivo, List<String> lineas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (String linea : lineas) {
                writer.write(linea);
                writer.newLine();
            }
            PantallaUtils pantallaUtils = new PantallaUtils();
            pantallaUtils.cerrarEstaPantalla(botonVolver);
            FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR.getDescripcion(), Constantes.TITULO_EDITAR_USUARIO.getDescripcion(), 600, 400);
            EditUserController editUserController = loader.getController();
            editUserController.initData(listaUsuarios, currentUser); // Inicia el controlador de la página de edición de usuarios
            showInfoAlert(Constantes.DATOS_MODIFICADOS.getDescripcion(), Constantes.USUARIO_ACTUALIZADO.getDescripcion()); // Muestra un mensaje de éxito
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lógica para el botón "Volver".
     * Permite volver a la pantalla anterior (sin realizar cambios).
     * @param actionEvent El evento generado por la acción del usuario (clic en el botón).
     * @throws IOException Si ocurre un error al cargar la pantalla anterior.
     */
    public void botonVolverActionPerformed(ActionEvent actionEvent) throws IOException {
        PantallaUtils pantallaUtils = new PantallaUtils();
        pantallaUtils.cerrarEstaPantalla(botonVolver); // Cierra la pantalla de edición de usuario
        FXMLLoader loader = pantallaUtils.showEstaPantalla(new Stage(), Constantes.PAGINA_MODIFICAR.getDescripcion(), Constantes.TITULO_EDITAR_USUARIO.getDescripcion(), 600, 400);
        EditUserController editUserController = loader.getController();
        editUserController.initData(listaUsuarios, currentUser); // Regresa a la pantalla de edición de usuarios
    }

    public void escribirBBDD (Usuario usuario){

        usuario.actualizarUsuario(usuario);

    }
}
