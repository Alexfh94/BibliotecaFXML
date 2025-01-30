package com.carballeira.practica1.model;

import java.util.ArrayList;

public interface DatabaseOperationsUsuario {

    // Método para insertar un usuario
    boolean insertarUsuario(Usuario usuario);

    boolean actualizarUsuario(Usuario usuario);

    // Método para eliminar un usuario
    boolean eliminarUsuario(String email);

    // Método para obtener todos los usuarios
    ArrayList<Usuario> obtenerTodosUsuarios();
}