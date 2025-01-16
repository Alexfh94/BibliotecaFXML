package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    public Label contadorSegundos; // Etiqueta para mostrar el contador de segundos
    @FXML
    private Label numeroPanel; // Etiqueta para mostrar el número aleatorio a adivinar

    @FXML
    private TextField numero; // Campo de texto para que el usuario ingrese el número

    @FXML
    private Button listo; // Botón para confirmar que el usuario ha ingresado el número

    @FXML
    private Button botonVolver; // Botón para volver a la pantalla anterior

    private Random r = new Random(); // Generador de números aleatorios
    private Usuario usuario; // Usuario que está registrando la cuenta

    /**
     * Inicia un hilo que actualiza el número aleatorio que se debe adivinar,
     * y un contador que varía entre 3 segundos de espera inicial y 7 segundos para generar un nuevo número.
     */
    private void startRandomNumberThread() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int countdown = 3; // Comenzamos con el retraso inicial de 3 segundos

            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (countdown > 0) {
                        // Actualiza el contador de segundos en la interfaz
                        contadorSegundos.setText("("+countdown + "s)");
                        countdown--;
                    } else {
                        // Genera un número aleatorio y lo muestra en el panel
                        int randomNumber = r.nextInt(9990) + 10;
                        numeroPanel.setText(String.valueOf(randomNumber));

                        // Reinicia el contador para los 7 segundos entre números
                        countdown = 7;
                    }
                });
            }
        }, 0, 1000); // Actualización cada 1 segundo
    }

    /**
     * Acción del botón "Listo". Verifica si el número ingresado es correcto y procede a crear la cuenta.
     * Si ocurre un error, se muestra un mensaje de error.
     */
    @FXML
    private void listoActionPerformed(ActionEvent event) {
        try{
            confirmarCaptcha(); // Verifica el número ingresado por el usuario
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir la traza del error
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
            alert.showAndWait();
        }
    }

    /**
     * Acción del botón "Volver". Cierra la pantalla actual y regresa a la pantalla principal.
     * @param event El evento generado por el usuario.
     * @throws IOException Si ocurre un error al cargar la pantalla de inicio.
     */
    @FXML
    private void botonVolverActionPerformed(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST)); // Cierra la ventana actual
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
        showErrorAlert("Se ha cancelado la creación de la cuenta", "Se ha cancelado la creación de la cuenta");
    }

    /**
     * Verifica si el número ingresado por el usuario es correcto.
     * Si es correcto, guarda los datos del usuario y lo redirige al menú principal.
     * Si es incorrecto, muestra un mensaje de error.
     * @throws IOException Si ocurre un error al guardar el usuario o cargar la pantalla.
     */
    private void confirmarCaptcha() throws IOException {
        try {
            if (numero.getText().equals(String.valueOf(numeroPanel.getText()))) { // Verifica si el número ingresado es correcto
                guardarUsuarioEnArchivo(usuario); // Guarda los datos del usuario
                PantallaUtils pantallaUtils = new PantallaUtils();
                Stage stage = new Stage();
                FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
                MenuController menuController = loader.getController();
                menuController.initData(usuario); // Pasa los datos del usuario al controlador del menú principal
                pantallaUtils.cerrarEstaPantalla(listo); // Cierra la pantalla actual
                showInfoAlert("Cuenta Creada", "Cuenta creada para el usuario: " + usuario.getNombre()); // Muestra mensaje de éxito
            } else {
                showErrorAlert("Error", "El número introducido es incorrecto. Inténtelo de nuevo."); // Muestra mensaje de error
            }
        } catch (IOException e) {
            e.printStackTrace(); // Imprimir la traza del error
            Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
            alert.showAndWait();
        }
    }

    /**
     * Inicializa los datos del usuario que se va a registrar.
     * @param usuario El usuario a registrar.
     */
    public void initData(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Método de inicialización. Inicia el hilo para generar los números aleatorios
     * y configura el evento para confirmar el CAPTCHA cuando el usuario presione "Enter" en el campo de texto.
     */
    @FXML
    public void initialize() {
        startRandomNumberThread(); // Inicia el hilo para generar números aleatorios

        numero.setOnAction(event -> { // Cuando el usuario presione "Enter"
            try {
                confirmarCaptcha(); // Verifica el número ingresado
            } catch (IOException e) {
                e.printStackTrace(); // Imprimir la traza del error
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de CAPTCHA.");
                alert.showAndWait();
            }
        });
    }

    /**
     * Guarda los datos del usuario en un archivo de texto "Usuarios.txt".
     * Si ocurre un error al guardar, se muestra un mensaje de error.
     * @param usuario El usuario a guardar en el archivo.
     */
    private void guardarUsuarioEnArchivo(Usuario usuario) {
        FileWriter fileWriter = null;

        try {
            // Abrir el archivo en modo de adición (si no existe, lo crea)
            fileWriter = new FileWriter("Usuarios.txt", true);

            // Guardamos los datos del usuario en el archivo (nombre, email, teléfono, contraseña y si es admin)
            fileWriter.write(usuario.getNombre() + "," + usuario.getEmail() + "," + usuario.getTelefono() + "," + usuario.getContraseña() + "," + usuario.getAdmin() + "\n");

            // Cerramos el FileWriter
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace(); // Imprimir la traza del error
            showErrorAlert("Error", "Ocurrió un error al guardar el usuario."); // Mostrar error al guardar los datos
        }
    }
}
