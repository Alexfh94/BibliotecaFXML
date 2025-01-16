package com.carballeira.practica1.controller;

import com.carballeira.practica1.model.Libro;
import com.carballeira.practica1.model.Usuario;
import com.carballeira.practica1.utils.AlertUtils;
import com.carballeira.practica1.utils.Constantes;
import com.carballeira.practica1.utils.PantallaUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static com.carballeira.practica1.utils.AlertUtils.showInfoAlert;

public class PrestamoController {

    private Usuario usuario;
    private ArrayList<Libro> listaLibros;
    private ArrayList<Libro> librosReservados = new ArrayList<>();

    @FXML
    private Button botonDevolver;

    @FXML
    private Button botonReservar;

    @FXML
    private Button botonVolver;

    @FXML
    private TableColumn<?, ?> colAutorDisponible;

    @FXML
    private TableColumn<?, ?> colAutorReservado;

    @FXML
    private TableColumn<?, ?> colAñoDisponible;

    @FXML
    private TableColumn<?, ?> colAñoReservado;

    @FXML
    private TableColumn<Libro, Boolean> colDisponible; // Cambia el tipo genérico a Boolean


    @FXML
    private TableColumn<?, ?> colFechaReservado;

    @FXML
    private TableColumn<?, ?> colTituloDisponible;

    @FXML
    private TableColumn<?, ?> colTituloReservado;

    @FXML
    private TableView<Libro> tablaDisponibles;

    @FXML
    private TableView<Libro> tablaReservados;



    @FXML
    public void initialize() {
        configurarTablaDisponibles();
        configureListeners();
        configurarTablaReservados();
    }

    @FXML
    void lendActionPerformed(ActionEvent event) {
        Libro selectedLibro = tablaDisponibles.getSelectionModel().getSelectedItem();
        if (selectedLibro == null) {
            AlertUtils.showErrorAlert("Error", "Por favor, selecciona un libro para reservarlo.");

            return;
        }

        if (!selectedLibro.getDisponible()){

            AlertUtils.showErrorAlert("Error", "El libro seleccionado no esta disponible.");

            return;

        }



        AlertUtils.showConfirmationAlert("Confirmar reserva", "¿Estás seguro de que deseas reservar el libro: " + selectedLibro.getTitulo(), () -> {


            selectedLibro.setEmailUsuarioReservado(usuario.getEmail());
            selectedLibro.setFechaDevolucion(getFechaActual());
            selectedLibro.setDisponible(false);

            for (int i = 0; i < listaLibros.size(); i++) {
                if (listaLibros.get(i).getTitulo().equals(selectedLibro.getTitulo())) {
                    listaLibros.set(i, selectedLibro);
                    break;
                }
            }

            librosReservados.add(selectedLibro);
            actualizarArchivoLibros();
            configurarTablaDisponibles();
            configurarTablaReservados();
            rellenarTablas();

        });

        showInfoAlert("Libro reservado", "Libro reservado correctamente");

    }


    @FXML
    void returnActionPerformed(ActionEvent event) {

        Libro selectedLibro = tablaReservados.getSelectionModel().getSelectedItem();
        if (selectedLibro == null) {
            AlertUtils.showErrorAlert("Error", "Por favor, selecciona un libro para reservarlo.");

            return;
        }




        AlertUtils.showConfirmationAlert("Confirmar devolucion", "¿Estás seguro de que deseas devolver el libro: " + selectedLibro.getTitulo(), () -> {

            selectedLibro.setEmailUsuarioReservado(null);
            selectedLibro.setFechaDevolucion(null);
            selectedLibro.setDisponible(true);

            for (int i = 0; i < listaLibros.size(); i++) {
                if (listaLibros.get(i).getTitulo().equals(selectedLibro.getTitulo())) {
                    listaLibros.set(i, selectedLibro);
                    break;
                }
            }

            librosReservados.remove(selectedLibro);
            actualizarArchivoLibros();
            configurarTablaDisponibles();
            configurarTablaReservados();
            rellenarTablas();

        });

        showInfoAlert("Libro Devuelto", "Libro devuelto correctamente");

    }

    @FXML
    void volverActionPerformed(ActionEvent event) throws IOException {

        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), "BIBLIOTECA", 400, 600);
        MenuController menuController = loader.getController();
        menuController.initSesion(usuario);
    }

    public void initData(Usuario usuario, ArrayList<Libro> listaLibros){

        this.usuario=usuario;
        this.listaLibros = listaLibros;
        rellenarTablas();
    }

    public void rellenarTablas() {

        if (listaLibros == null || listaLibros.isEmpty()) {
            System.out.println("No hay libros para mostrar.");
            return;
        }

        // Limpiar la lista de libros reservados para evitar duplicados
        librosReservados.clear();

        // Iterar sobre la lista de libros
        for (Libro libro : listaLibros) {
            // Verificar si el emailUsuarioReservado no es null y coincide con el email del usuario actual
            if (libro.getEmailUsuarioReservado() != null &&
                    !libro.getEmailUsuarioReservado().isEmpty() &&
                    libro.getEmailUsuarioReservado().equals(usuario.getEmail())) {

                librosReservados.add(libro);
            }
        }

        // Crear y asignar las listas observables
        ObservableList<Libro> librosList = FXCollections.observableArrayList(listaLibros);
        tablaDisponibles.setItems(librosList);

        ObservableList<Libro> reservadosList = FXCollections.observableArrayList(librosReservados);
        tablaReservados.setItems(reservadosList); // Asignar los libros reservados a la tabla correspondiente
    }


    private void configurarTablaDisponibles() {
        colTituloDisponible.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutorDisponible.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAñoDisponible.setCellValueFactory(new PropertyValueFactory<>("añoPublicacion"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        colDisponible.setCellFactory(column -> new TableCell<Libro, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    if (item) {
                        // Crear un ícono verde con un tick
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/carballeira/practica1/img/confirm.png"))));
                        imageView.setFitWidth(20); // Ajusta el tamaño del ícono
                        imageView.setFitHeight(20);
                        setGraphic(imageView);
                        setStyle("-fx-alignment: CENTER;");
                        setText(null);
                    } else {
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/carballeira/practica1/img/denied.jpg"))));
                        imageView.setFitWidth(20); // Ajusta el tamaño del ícono
                        imageView.setFitHeight(20);
                        setGraphic(imageView);
                        setStyle("-fx-alignment: CENTER;");
                        setText(null);
                    }
                }
            }
        });
        tablaDisponibles.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void configurarTablaReservados() {
        colTituloReservado.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutorReservado.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAñoReservado.setCellValueFactory(new PropertyValueFactory<>("añoPublicacion"));
        colFechaReservado.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        tablaReservados.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    private void configureListeners() {
        // Listener para tablaDisponibles
        tablaDisponibles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Deseleccionar el elemento de la tablaReservados
                tablaReservados.getSelectionModel().clearSelection();
                botonReservar.setDisable(false); // Habilitar botón Reservar si se selecciona un libro
            } else {
                botonReservar.setDisable(true); // Deshabilitar botón si no hay selección
            }
        });

        // Listener para tablaReservados
        tablaReservados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Deseleccionar el elemento de la tablaDisponibles
                tablaDisponibles.getSelectionModel().clearSelection();
                botonDevolver.setDisable(false); // Habilitar botón Devolver si se selecciona un libro
            } else {
                botonDevolver.setDisable(true); // Deshabilitar botón si no hay selección
            }
        });
    }

    private String getFechaActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatter);
    }

    private void actualizarArchivoLibros() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Libros.txt"))) {
            for (Libro libro : listaLibros) {
                if (libro.getEmailUsuarioReservado()==null || libro.getFechaDevolucion() ==null) {
                    writer.write(libro.getTitulo() + "," + libro.getAutor() + "," + libro.getAñoPublicacion()
                            + "," + libro.getDisponible() + "\n");

                }else{
                    writer.write(libro.getTitulo() + "," + libro.getAutor() + "," + libro.getAñoPublicacion()
                            + "," + libro.getDisponible() + "," + libro.getFechaDevolucion() + "," + libro.getEmailUsuarioReservado() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
