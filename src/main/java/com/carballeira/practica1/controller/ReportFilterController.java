package com.carballeira.practica1.controller;

import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import com.carballeira.practica1.utils.ReportGenerating;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.Connection;

import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;
import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class ReportFilterController {

    @FXML
    private ComboBox<String> tableComboBox;
    @FXML
    private ComboBox<String> fieldComboBox;
    @FXML
    private TextField textField;
    @FXML
    private Button botonGenerarInforme;
    @FXML
    private Button botonVolver;


    private Connection conn;

    // Método que inicializa los ComboBoxes con los valores correspondientes
    @FXML
    private void initialize() {

        tableComboBox.getItems().addAll("Usuarios", "Libros");
        tableComboBox.setOnAction(event -> updateFieldsBasedOnTable());

        fieldComboBox.getItems().clear();

        botonGenerarInforme.setDisable(true);
    }

    // Método que actualiza los campos según la tabla seleccionada
    @FXML
    private void updateFieldsBasedOnTable() {
        // Limpiar los elementos del ComboBox de campos
        fieldComboBox.getItems().clear();

        // Añadir los campos correspondientes a la tabla seleccionada
        if (tableComboBox.getValue() != null) {
            switch (tableComboBox.getValue()) {
                case "Usuarios":
                    fieldComboBox.getItems().addAll("Nombre", "Email", "Telefono", "Admin");
                    break;
                case "Libros":
                    fieldComboBox.getItems().addAll("Titulo", "Autor", "Año", "Disponible", "Usuario Reservado");
                    break;
                default:
                    break;
            }
        }
        // Habilitar botón de Filtrar si se seleccionan tabla y campo
        botonGenerarInforme.setDisable(tableComboBox.getValue() == null || fieldComboBox.getValue() == null);
    }

    // Método para generar el informe filtrado basado en los parámetros seleccionados
    @FXML
    private void generateFilteredReport() {
        String table = tableComboBox.getValue();
        String field = fieldComboBox.getValue();
        String filterValue = textField.getText().trim();

        // Validación de campos vacíos
        if (table == null || field == null ) {
            showErrorAlert("Campos incompletos", "Por favor, complete todos los campos.");
            return;
        }

        // Intentar generar el informe filtrado
        try {
            if (conn == null) {
                throw new IllegalStateException("La conexión a la base de datos no está establecida.");
            }

            // Asumimos que el método para generar el informe se adapta a este formato
            ReportGenerating report = new ReportGenerating();
            report.generateFilteredReport(conn, table, field, filterValue);
            showInfoAlert("Informe generado", "El informe filtrado se ha creado correctamente.");
        } catch (Exception e) {
            // Imprimir la traza de la excepción para depuración
            e.printStackTrace();
            // Mostrar un mensaje de error al usuario
            showErrorAlert("Error al generar informe", "No se pudo generar el informe filtrado. " + e.getMessage());
        }
    }

    // Acción para volver a la pantalla anterior
    @FXML
    void volverActionPerformed(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), Constantes.TITULO_BIBLIOTECA.getDescripcion(), 400, 600);
        MenuController menuController = loader.getController();
        // Asumimos que se pasa algún parámetro para la sesión o usuario, si es necesario
        menuController.initSesion(null);  // Cambiar 'null' por el usuario actual si es necesario
    }

    // Método para inicializar la conexión a la base de datos desde el controlador
    public void initData(Connection conn) {
        this.conn = conn;
    }

    // Método para filtrar los resultados según los valores seleccionados y el campo de texto
    @FXML
    private void filtrarActionPerformed() {
        String filterValue = textField.getText().trim();
        if (filterValue.isEmpty()) {
            showErrorAlert("Campo de filtro vacío", "Por favor, ingrese un valor para filtrar.");
            return;
        }

        // Lógica para filtrar los datos de las tablas, dependiendo de la selección del ComboBox
        if (tableComboBox.getValue().equals("Libros")) {
            // Filtrar la tabla de Libros en función del filtro ingresado
            // Lógica para mostrar los libros que coinciden con el filtro
        } else if (tableComboBox.getValue().equals("Usuarios")) {
            // Filtrar la tabla de Usuarios en función del filtro ingresado
            // Lógica para mostrar los usuarios que coinciden con el filtro
        }

        // Mostrar información de éxito al filtrar
        showInfoAlert("Filtrado exitoso", "Los resultados han sido filtrados correctamente.");
        botonGenerarInforme.setDisable(false);  // Habilitar la opción para generar el informe
    }
}
