package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.AlertUtils;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
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
import javafx.stage.WindowEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.carballeira.practica1.utils.AlertUtils.*;

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
    private Usuario usuario;


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
    private void botonVolverActionPerformed(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
        showErrorAlert("Se ha cancelado la creación de la cuenta", "Se ha cancelado la creación de la cuenta");

    }

    private void confirmarCaptcha() throws IOException {
        try {
            if (numero.getText().equals(String.valueOf(numeroPanel.getText()))) {
                guardarUsuarioEnArchivo(usuario);
                PantallaUtils pantallaUtils = new PantallaUtils();
                Stage stage = new Stage();
                FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
                MenuController menuController = loader.getController();
                menuController.initData(usuario);
                pantallaUtils.cerrarEstaPantalla(listo);
                showInfoAlert("Cuenta Creada", "Cuenta creada para el usuario: " + usuario.getNombre());
            } else {

                showErrorAlert("Error", "El número introducido es incorrecto. Inténtelo de nuevo.");

            }
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir la traza del error
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
            alert.showAndWait();
        }
    }

    public void initData(Usuario usuario) {
     this.usuario=usuario;
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

    private void guardarUsuarioEnArchivo(Usuario usuario) {
        FileWriter fileWriter = null;

        try {
            // Abrir el archivo en modo de adición (si no existe, lo crea)
            fileWriter = new FileWriter("Usuarios.txt", true);

            // Guardamos los datos del usuario en el archivo (nombre y contraseña)
            fileWriter.write(usuario.getNombre() + "," + usuario.getEmail() + "," + usuario.getTelefono()+ "," + usuario.getContraseña()+ ","+ usuario.getAdmin()+"\n");

            // Cerramos el FileWriter
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Error", "Ocurrió un error al guardar el usuario.");
        }
    }


}
