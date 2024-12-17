package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CaptchaController {

    @FXML
    public Label contadorSegundos;
    @FXML
    private Label numeroPanel;

    @FXML
    private TextField numero;

    @FXML
    private Button listo;

    @FXML
    private Button botonVolver;

    private Random r = new Random();
    private String s1, s2, s3, s4;


    private void startRandomNumberThread() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int countdown = 3; // Comenzamos con el retraso inicial de 3 segundos

            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (countdown > 0) {
                        // Actualiza el contador de segundos
                        contadorSegundos.setText("("+countdown + "s)");
                        countdown--;
                    } else {
                        // Genera un número aleatorio y actualiza el label
                        int randomNumber = r.nextInt(9990) + 10;
                        numeroPanel.setText(String.valueOf(randomNumber));

                        // Reinicia el contador para los 7 segundos entre números
                        countdown = 7;
                    }
                });
            }
        }, 0, 1000); // Actualización cada 1 segundo
    }


    @FXML
    private void listoActionPerformed(ActionEvent event) {
        try{
        confirmarCaptcha();
    } catch (IOException e) {
        e.printStackTrace(); // Imprimir la traza del error
        Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
        alert.showAndWait();
    }
    }

    @FXML
    private void botonVolverActionPerformed(ActionEvent event) {
        // Aquí puedes manejar la acción de volver
    }

    private void confirmarCaptcha() throws IOException {
        try {
        if (numero.getText().equals(String.valueOf(numeroPanel.getText()))) {
            Usuario usuario = new Usuario(s1, s2, s3, s4, "n");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/carballeira/practica1/menu-view.fxml"));
            Parent root = loader.load();
            MenuController menuController = loader.getController();
            menuController.initData(usuario);

            Stage stage = new Stage();

            Scene scene = new Scene(root, 400, 600);

            stage.setScene(scene);

            stage.setTitle("BIBLIOTECA");

            stage.show();

            Stage currentStage = (Stage) listo.getScene().getWindow();
            currentStage.hide();

        } else {


        }
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir la traza del error
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
            alert.showAndWait();
        }
    }

    public void initData(String s1, String s2, String s3, String s4) {
        // Constructor para recibir los parámetros de la creación de cuenta.
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
    }

    @FXML
    public void initialize() {

        startRandomNumberThread();

        numero.setOnAction(event -> {
            try {
                confirmarCaptcha();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
                alert.showAndWait();
            }
        });
    }

}
