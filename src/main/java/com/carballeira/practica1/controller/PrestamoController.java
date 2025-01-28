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
    private Libro selectedLibro;

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
    private TableColumn<Libro, Boolean> colDisponible;

    @FXML
    private TableColumn<?, ?> colFechaReservado;

    @FXML
    private TableColumn<?, ?> colFechaDevolucion;

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
        selectedLibro = tablaDisponibles.getSelectionModel().getSelectedItem();

        if (selectedLibro == null) {
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.POR_FAVOR_SELECCIONA_LIBRO.getDescripcion());
            return;
        }

        if (!selectedLibro.getDisponible()) {
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.LIBRO_NO_DISPONIBLE.getDescripcion());
            return;
        }

        AlertUtils.showConfirmationAlert(Constantes.CONFIRMAR_RESERVA.getDescripcion(), Constantes.CONFIRMAR_RESERVA_MSG.getDescripcion() + selectedLibro.getTitulo(), () -> {
            selectedLibro.setEmailUsuarioReservado(usuario.getEmail());
            selectedLibro.setFechaPrestamo(getFechaActual());
            selectedLibro.setFechaDevolucion(getFechaEnDosSemanas());
            selectedLibro.setDisponible(false);

            for (int i = 0; i < listaLibros.size(); i++) {
                if (listaLibros.get(i).getTitulo().equals(selectedLibro.getTitulo())) {
                    listaLibros.set(i, selectedLibro);
                    break;
                }
            }

            librosReservados.add(selectedLibro);
            actualizarDbLibros();
            configurarTablaDisponibles();
            configurarTablaReservados();
            rellenarTablas();
        });

        showInfoAlert(Constantes.LIBRO_RESERVADO.getDescripcion(), Constantes.LIBRO_RESERVADO_MSG.getDescripcion());
    }

    @FXML
    void returnActionPerformed(ActionEvent event) {
        selectedLibro = tablaReservados.getSelectionModel().getSelectedItem();
        if (selectedLibro == null) {
            AlertUtils.showErrorAlert(Constantes.ERROR_TITULO.getDescripcion(), Constantes.POR_FAVOR_SELECCIONA_LIBRO_DEVOLVER.getDescripcion());
            return;
        }

        AlertUtils.showConfirmationAlert(Constantes.CONFIRMAR_DEVOLUCION.getDescripcion(), Constantes.CONFIRMAR_DEVOLUCION_MSG.getDescripcion() + selectedLibro.getTitulo(), () -> {
            selectedLibro.setEmailUsuarioReservado(null);
            selectedLibro.setFechaPrestamo(null);
            selectedLibro.setFechaPrestamo(null);
            selectedLibro.setDisponible(true);

            for (int i = 0; i < listaLibros.size(); i++) {
                if (listaLibros.get(i).getTitulo().equals(selectedLibro.getTitulo())) {
                    listaLibros.set(i, selectedLibro);
                    break;
                }
            }

            librosReservados.remove(selectedLibro);
            actualizarDbLibros();
            configurarTablaDisponibles();
            configurarTablaReservados();
            rellenarTablas();
        });

        showInfoAlert(Constantes.LIBRO_DEVUELTO.getDescripcion(), Constantes.LIBRO_DEVUELTO_MSG.getDescripcion());
    }

    @FXML
    void volverActionPerformed(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) botonVolver.getScene().getWindow();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        PantallaUtils pantallaUtils = new PantallaUtils();
        Stage stage = new Stage();
        FXMLLoader loader = pantallaUtils.showEstaPantalla(stage, Constantes.PAGINA_INICIAL.getDescripcion(), Constantes.TITULO_BIBLIOTECA.getDescripcion(), 400, 600);
        MenuController menuController = loader.getController();
        menuController.initSesion(usuario);
    }

    public void initData(Usuario usuario, ArrayList<Libro> listaLibros) {
        this.usuario = usuario;
        this.listaLibros = listaLibros;
        rellenarTablas();
    }

    public void rellenarTablas() {
        if (listaLibros == null || listaLibros.isEmpty()) {
            System.out.println(Constantes.NO_HAY_LIBROS.getDescripcion());
            return;
        }

        librosReservados.clear();

        for (Libro libro : listaLibros) {
            if (libro.getEmailUsuarioReservado() != null &&
                    !libro.getEmailUsuarioReservado().isEmpty() &&
                    libro.getEmailUsuarioReservado().equals(usuario.getEmail())) {
                librosReservados.add(libro);
            }
        }

        ObservableList<Libro> librosList = FXCollections.observableArrayList(listaLibros);
        tablaDisponibles.setItems(librosList);

        ObservableList<Libro> reservadosList = FXCollections.observableArrayList(librosReservados);
        tablaReservados.setItems(reservadosList);
    }

    private void configurarTablaDisponibles() {
        // Configura las columnas de la tabla de libros disponibles
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
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constantes.ICONO_CONFIRM.getDescripcion()))));
                        imageView.setFitWidth(20); // Ajusta el tamaño del ícono
                        imageView.setFitHeight(20);
                        setGraphic(imageView);
                        setStyle("-fx-alignment: CENTER;");
                        setText(null);
                    } else {
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constantes.ICONO_DENY.getDescripcion()))));
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
        colFechaReservado.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

        tablaReservados.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void configureListeners() {
        tablaDisponibles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tablaReservados.getSelectionModel().clearSelection();
                botonReservar.setDisable(false);
            } else {
                botonReservar.setDisable(true);
            }
        });

        tablaReservados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tablaDisponibles.getSelectionModel().clearSelection();
                botonDevolver.setDisable(false);
            } else {
                botonDevolver.setDisable(true);
            }
        });
    }

    private String getFechaActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA.getDescripcion());
        return LocalDate.now().format(formatter);
    }

    private String getFechaEnDosSemanas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constantes.FORMATO_FECHA.getDescripcion());
        return LocalDate.now().plusDays(14).format(formatter);
    }

    private void actualizarArchivoLibros() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constantes.ARCHIVO_LIBROS.getDescripcion()))) {
            for (Libro libro : listaLibros) {
                if (libro.getEmailUsuarioReservado() == null || libro.getFechaPrestamo() == null) {
                    writer.write(libro.getTitulo() + "," + libro.getAutor() + "," + libro.getAñoPublicacion()
                            + "," + libro.getDisponible() + "\n");
                } else {
                    writer.write(libro.getTitulo() + "," + libro.getAutor() + "," + libro.getAñoPublicacion()
                            + "," + libro.getDisponible() + "," + libro.getFechaPrestamo() + "," + libro.getFechaDevolucion() + "," + libro.getEmailUsuarioReservado() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void actualizarDbLibros(){

        selectedLibro.actualizarLibro(selectedLibro);

    }
}
