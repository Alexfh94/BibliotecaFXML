package com.carballeira.practica1.model;

import java.util.List;

public interface DatabaseOperationsLibro {

    // Método para insertar un usuario
    boolean insertarLibro(Libro libro);

    boolean actualizarLibro(Libro libro);

    // Método para eliminar un usuario
    boolean eliminarLibro(Libro libro);

    // Método para obtener todos los usuarios
    List<Libro> obtenerTodosLibros();
}