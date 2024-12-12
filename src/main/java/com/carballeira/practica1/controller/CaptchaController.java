package com.carballeira.practica1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.application.Platform;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CaptchaController {

    @FXML
    private Label numeroPanel;

    @FXML
    private TextField numero;

    @FXML
    private Button listo;

    @FXML
    private Button botonVolver;

    private Random r = new Random();
    private int numero1;
    private String s1, s2, s3, s4;


    private void startRandomNumberThread() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Retraso de 3 segundos antes de mostrar el primer número
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Generar un número aleatorio y actualizar el label
                int randomNumber = r.nextInt(9990) + 10;
                Platform.runLater(() -> numeroPanel.setText(String.valueOf(randomNumber)));
            }
        }, 0, 7000);  // El primer número aparecerá después de 3 segundos y luego cada 7 segundos
    }

    @FXML
    private void listoActionPerformed(ActionEvent event) {
        confirmarCaptcha();
    }

    @FXML
    private void botonVolverActionPerformed(ActionEvent event) {
        // Aquí puedes manejar la acción de volver
    }

    private void confirmarCaptcha() {
        if (numero.getText().equals(String.valueOf(numeroPanel.getText()))) {
            // Si la respuesta es correcta
            System.out.println("Captcha correcto!");
            // Realiza alguna acción adicional, como continuar al siguiente paso
        } else {
            // Si la respuesta es incorrecta
            System.out.println("Captcha incorrecto!");
            // Realiza alguna acción adicional, como mostrar un mensaje de error
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

        // Acción de confirmar al presionar "Enter" en el campo de texto del número
        numero.setOnAction(event -> confirmarCaptcha());
    }
}
