package com.carballeira.practica1.utils;

/**
 * Esta clase es un enumerado para albergar las constantes de la aplicación.
 */
public enum Constantes {
    PAGINA_INICIAL("/com/carballeira/practica1/menu-view.fxml"),
    PAGINA_REGISTRO("/com/carballeira/practica1/register-view.fxml"),
    PAGINA_LOGIN("/com/carballeira/practica1/login-view.fxml"),
    PAGINA_CAPTCHA("/com/carballeira/practica1/captcha-view.fxml"),
    PAGINA_DEVOLUCION("/com/carballeira/practica1/devolucion-view.fxml"),
    PAGINA_PRESTAMO("/com/carballeira/practica1/prestamo-view.fxml"),
    PAGINA_MODIFICAR("/com/carballeira/practica1/edit-user-view.fxml"),
    PAGINA_MODIFICAR2("/com/carballeira/practica1/edit-user-view2.fxml");


    private final String descripcion;

    // Constructor para asociar una descripción a cada constante
    Constantes(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
