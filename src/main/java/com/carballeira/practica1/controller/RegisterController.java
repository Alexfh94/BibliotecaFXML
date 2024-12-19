package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;
import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class RegisterController {

    @FXML
    private TextField nombre, email, telefono;

    @FXML
    private PasswordField contraseña;

    @FXML
    private TextField contraseñaVisible;

    @FXML
    private Button crearUsuario, botonVolver, mostrarContraseña;


    private int contador = 0;
    private int contador2=0;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    @FXML
    private void initialize() {
        mostrarContraseña.setDisable(true);

        contraseña.textProperty().addListener((observable, oldValue, newValue) -> actualizarEstadoBoton());

        nombre.setOnAction(this::crearUsuario);
        email.setOnAction(this::crearUsuario);
        telefono.setOnAction(this::crearUsuario);
        contraseña.setOnAction(this::crearUsuario);

        // Acción de tecla ESC para volver
        botonVolver.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                botonVolver.fire();
            }
        });

        botonVolver.setOnAction(event -> {
            try {
                botonVolverAction();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void crearUsuario(ActionEvent event) {
        String nombre1 = nombre.getText();
        String email1 = email.getText();
        String telefono1 = telefono.getText();
        String contraseña1 = contraseña.getText();
        contador2 = 0;
        Usuario usuario = new Usuario(nombre1,email1,telefono1,contraseña1,"n");

        //comprobar si existe el usuario previamente
        for (int i = 0; i < listaUsuarios.size(); i++) {

            if (listaUsuarios.get(i).getNombre().equals(usuario.getNombre())) {
                contador2++;
            }
        }

        if (usuario.validarCampos(nombre1, email1, telefono1, contraseña1)) {


            if (contador2 > 0) {
                showErrorAlert("Error", "El usuario que intenta crear ya existe");
                contador2 = 0;
            } else {


                try {
                    PantallaUtils pantallaUtils = new PantallaUtils();
                    Stage stage = new Stage();
                    FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_CAPTCHA.getDescripcion(), "CAPTCHA", 400, 600);
                    CaptchaController captchaController = loader.getController();
                    captchaController.initData(usuario);
                    pantallaUtils.cerrarEstaPantalla(botonVolver);

                } catch (IOException e) {
                    e.printStackTrace(); // Imprimir la traza del error
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    private void mostrarContraseña(ActionEvent event) {
        // Alternar entre mostrar y ocultar la contraseña
        if (contador % 2 == 0) {
            // Mostrar contraseña: establecer el TextField visible y el PasswordField invisible
            contraseñaVisible.setText(contraseña.getText());
            contraseña.setVisible(false);
            contraseñaVisible.setVisible(true);
            mostrarContraseña.setText("Ocultar");
        } else {
            // Ocultar contraseña: volver a mostrar el PasswordField y ocultar el TextField
            contraseña.setText(contraseñaVisible.getText());
            contraseña.setVisible(true);
            contraseñaVisible.setVisible(false);
            mostrarContraseña.setText("Mostrar");
        }
        contador++;
    }

    @FXML
    private void botonVolverAction() throws IOException {

        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
    }

    private void actualizarEstadoBoton() {
        mostrarContraseña.setDisable(contraseña.getText().isEmpty());
    }

    public void initData(ArrayList<Usuario> listaUsuarios){

        this.listaUsuarios = listaUsuarios;
    }
}
