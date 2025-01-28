package com.carballeira.practica1.model;

import java.util.ArrayList;
import java.sql.*;


import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;

public class Usuario implements DatabaseOperationsUsuario {

    private String nombre;
    private String email;
    private String telefono;
    private String contraseña;
    private String admin;

    public Usuario(String nombre, String email, String telefono, String contraseña, String admin) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.admin = admin;
    }

    public Usuario() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public boolean validarCampos(String nombre, String email, String telefono, String contraseña) {
        if(nombre.contains(",")|| email.contains(",") ||telefono.contains(",") || contraseña.contains(",")){
            showErrorAlert("Error", "Ningún campo puede contener comas");
            return false;
        }
        if (nombre.isEmpty()) {
            showErrorAlert("Error", "El nombre no puede estar vacío.");
            return false;
        }
        if (!email.contains("@")) {
            showErrorAlert("Error", "El formato del email es incorrecto. Debe tener el formato 'ejemplo@ejemplo.com'");
            return false;
        }
        if (!telefono.matches("[1-9]+") || telefono.length() != 9) {
            showErrorAlert("Error", "El teléfono debe tener 9 dígitos y solo contener números.");
            return false;
        }
        if (contraseña.isEmpty()) {
            showErrorAlert("Error", "La contraseña no puede estar vacía.");
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", admin='" + admin + '\'' +
                '}';
    }

    @Override
    public boolean insertarUsuario(Usuario usuario) {

                String url = "jdbc:sqlite:data/bbdd_practica1.db";

                try (Connection conn = DriverManager.getConnection(url)) {
                    // Sentencia SQL para insertar una nueva fila
                    String sql = "INSERT INTO USUARIOS (NOMBRE, EMAIL, TELEFONO, CONTRASEÑA, ADMIN) VALUES (?, ?, ?, ?, ?)";
                    // Preparar la sentencia con los valores
                    try (PreparedStatement pstmt =
                                 conn.prepareStatement(sql)) {
                        pstmt.setString(1, usuario.getNombre());
                        pstmt.setString(2, usuario.getEmail());
                        pstmt.setString(3, usuario.getTelefono());
                        pstmt.setString(4, usuario.getContraseña());
                        pstmt.setString(5, usuario.getAdmin());
                        int filasAfectadas = pstmt.executeUpdate();
                        System.out.println("Filas insertadas: " +
                                filasAfectadas);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        return false;
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        String url = "jdbc:sqlite:data/bbdd_practica1.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            // Sentencia SQL para actualizar un usuario existente
            String sql = "UPDATE USUARIOS SET NOMBRE = ?, EMAIL = ?, TELEFONO = ?, CONTRASEÑA = ?, ADMIN = ? WHERE EMAIL = ?";

            // Preparar la sentencia con los valores
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, usuario.getNombre());
                pstmt.setString(2, usuario.getEmail());
                pstmt.setString(3, usuario.getTelefono());
                pstmt.setString(4, usuario.getContraseña());
                pstmt.setString(5, usuario.getAdmin());
                pstmt.setString(6, usuario.getEmail());

                // Ejecutar la actualización
                int filasAfectadas = pstmt.executeUpdate();
                System.out.println("Filas actualizadas: " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean eliminarUsuario(String email)  {

        String url = "jdbc:sqlite:data/bbdd_practica1.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            // Sentencia SQL para actualizar un usuario existente
            String sql = "DELETE FROM USUARIOS WHERE EMAIL = ?";

            // Preparar la sentencia con los valores
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                int filasAfectadas = pstmt.executeUpdate();
                System.out.println("Filas actualizadas: " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public ArrayList<Usuario> obtenerTodosUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT NOMBRE, EMAIL, TELEFONO, CONTRASEÑA, ADMIN FROM USUARIOS";
        String url = "jdbc:sqlite:data/bbdd_practica1.db";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setEmail(rs.getString("EMAIL"));
                usuario.setTelefono(rs.getString("TELEFONO"));
                usuario.setContraseña(rs.getString("CONTRASEÑA"));
                usuario.setAdmin(rs.getString("ADMIN"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }
}
