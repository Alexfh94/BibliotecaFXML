package com.carballeira.practica1.utils;

public enum Constantes {
    // Rutas de vistas (FXML)
    PAGINA_INICIAL("/com/carballeira/practica1/menu-view.fxml"),
    PAGINA_REGISTRO("/com/carballeira/practica1/register-view.fxml"),
    PAGINA_LOGIN("/com/carballeira/practica1/login-view.fxml"),
    PAGINA_CAPTCHA("/com/carballeira/practica1/captcha-view.fxml"),
    PAGINA_PRESTAMO("/com/carballeira/practica1/prestamo-view.fxml"),
    PAGINA_MODIFICAR("/com/carballeira/practica1/edit-user-view.fxml"),
    PAGINA_MODIFICAR2("/com/carballeira/practica1/edit-user-view2.fxml"),
    PAGINA_FILTRO("/com/carballeira/practica1/filtered-report.fxml"),

    // Archivos
    ARCHIVO_USUARIOS("Usuarios.txt"),
    ARCHIVO_LIBROS("Libros.txt"),
    RUTA_BBDD("jdbc:sqlite:data/bbdd_practica1.db"),

    // Títulos de ventanas
    TITULO_LOGIN("Iniciar Sesion"),
    TITULO_REGISTRO("Registro"),
    TITULO_PRESTAMO("Realizar prestamos y devoluciones"),
    TITULO_EDITAR_USUARIO("Editar Usuarios"),
    TITULO_REPORTE_FILTRADO("Generar Reporte Filtrado"),
    TITULO_CAPTCHA("CAPTCHA"),
    TITULO_BIBLIOTECA("BIBLIOTECA"),

    // Mensajes
    ERROR_VENTANA("Error al cargar ventana"),
    ERROR_INICIO_SESION("No se pudo abrir la ventana de inicio de sesión."),
    ERROR_REGISTRO("No se pudo abrir la ventana de registro."),
    ERROR_EDITAR_USUARIO("No se pudo abrir la ventana de edición de usuario."),
    ERROR_VENTANA_REPORTE("No se ha podido abrir la ventana de filtrad de reportes"),
    ERROR_ARCHIVO("Error al cargar archivo"),
    ERROR_CARGAR_USUARIOS("No se pudo cargar el archivo de usuarios."),
    ERROR_CARGAR_LIBROS("No se pudo cargar el archivo de libros."),
    ERROR_VENTANA_CAPTCHA("No se pudo abrir la ventana de CAPTCHA."),
    ERROR_USUARIO_EXISTE("El usuario que intenta crear ya existe"),
    ERROR_USUARIO_NOEXISTE("El usuario no existe"),
    ERROR_TITULO("Error"),
    CONFIRMAR_CIERRE("Confirmar cierre"),
    CONFIRMAR_CIERRE_MSG("¿Estás seguro de que deseas cerrar la sesión actual?"),
    CONFIRMAR_SALIDA("Confirmar salida"),
    CONFIRMAR_SALIDA_MSG("¿Estás seguro de que deseas salir del programa?"),
    SESION_CERRADA("Sesión cerrada"),
    SESION_CERRADA_MSG("Sesión cerrada correctamente."),
    SESION_INICIADA("Sesión iniciada"),
    SESION_INICIADA_MSG("Sesión iniciada correctamente."),
    USUARIO_NO_SESION("Usuario Actual: Inicie Sesión"),
    USUARIO_ACTUAL("Usuario Actual: "),
    NO_HAY_LIBROS("No hay libros para mostrar."),
    POR_FAVOR_SELECCIONA_LIBRO("Por favor, selecciona un libro para reservarlo."),
    LIBRO_NO_DISPONIBLE("El libro seleccionado no está disponible."),
    CONFIRMAR_RESERVA("Confirmar reserva"),
    CONFIRMAR_RESERVA_MSG("¿Estás seguro de que deseas reservar el libro: "),
    LIBRO_RESERVADO("Libro reservado"),
    LIBRO_RESERVADO_MSG("Libro reservado correctamente"),
    POR_FAVOR_SELECCIONA_LIBRO_DEVOLVER("Por favor, selecciona un libro para devolverlo."),
    CONFIRMAR_DEVOLUCION("Confirmar devolución"),
    CONFIRMAR_DEVOLUCION_MSG("¿Estás seguro de que deseas devolver el libro: "),
    LIBRO_DEVUELTO("Libro Devuelto"),
    LIBRO_DEVUELTO_MSG("Libro devuelto correctamente"),
    FORMATO_FECHA("dd/MM/yyyy"),
    ICONO_CONFIRM("/com/carballeira/practica1/img/confirm.png"),
    ICONO_DENY("/com/carballeira/practica1/img/deny.jpg"),
    MOSTRAR("Mostrar"),
    OCULTAR("Ocultar"),
    ES_ADMIN("s"),
    NO_ADMIN("n"),
    ERROR_EDITAR_USUARIO_SELECCIONADO("Por favor, selecciona un usuario para modificar."),
    CONFIRMAR_MODIFICACION("Confirmar modificación"),
    CONFIRMAR_MODIFICACION_MSG("¿Estás seguro de que deseas modificar los datos del usuario: "),
    ERROR_LECTURA_ARCHIVO("Ocurrió un error al leer el archivo."),
    ERROR_ESCRITURA_ARCHIVO("Ocurrió un error al escribir el archivo."),
    USUARIO_ELIMINADO("Usuario eliminado"),
    USUARIO_ELIMINADO_MSG("Usuario eliminado correctamente"),
    ERROR_ABRIR_MODIFICACION("No se pudo abrir la ventana de modificación: "),
    ERROR_ELIMINAR_USUARIO("Por favor, selecciona un usuario para borrar."),
    CONFIRMAR_BORRADO("Confirmar borrado"),
    NO_HAY_USUARIOS("No hay usuarios para mostrar."),
    CONFIRMAR_BORRADO_MSG("¿Estás seguro de que deseas eliminar el usuario: "),
    DATOS_MODIFICADOS("Datos modificados"),
    USUARIO_ACTUALIZADO("Usuario actualizado correctamente");

    private final String descripcion;

    Constantes(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
