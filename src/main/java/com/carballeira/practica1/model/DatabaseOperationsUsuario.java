package com.carballeira.practica1.model;

import java.util.List;

public interface DatabaseOperationsUsuario {

    // Método para insertar un usuario
    boolean insertarUsuario(Usuario usuario);

    boolean actualizarUsuario(Usuario usuario);

    // Método para eliminar un usuario
    boolean eliminarUsuario(String email);

    // Método para obtener todos los usuarios
    List<Usuario> obtenerTodosUsuarios();
}