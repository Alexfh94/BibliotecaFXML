package com.carballeira.practica1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Libro implements DatabaseOperationsLibro {

    private String titulo;
    private String autor;
    private String añoPublicacion;
    private Boolean disponible;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String emailUsuarioReservado;
    private int id;


    public Libro() {

    }

    public Libro(String titulo, String autor, String añoPublicacion, Boolean disponible, String fechaPrestamo, String fechaDevolucion, String emailUsuarioReservado) {
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.disponible = disponible;
        this.fechaPrestamo = fechaDevolucion;
        this.fechaDevolucion = fechaDevolucion;
        this.emailUsuarioReservado=emailUsuarioReservado;
    }

    public Libro(String titulo, String autor, String añoPublicacion, Boolean disponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.disponible = disponible;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAñoPublicacion(String añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getEmailUsuarioReservado() {
        return emailUsuarioReservado;
    }

    public void setEmailUsuarioReservado(String emailUsuarioReservado) {
        this.emailUsuarioReservado = emailUsuarioReservado;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isDisponible(){

        if (disponible=true)
            return true;
        else
            return false;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean insertarLibro(Libro libro) {
        return false;
    }

    @Override
    public boolean actualizarLibro(Libro libro) {
        String url = "jdbc:sqlite:data/bbdd_practica1.db";

        try (Connection conn = DriverManager.getConnection(url)) {

            // Sentencia SQL para actualizar un libro existente
            String sql = "UPDATE LIBROS SET Titulo = ?, Autor = ?, AñoPublicacion = ?, Disponible = ?, FechaPrestamo = ?, FechaDevolucion = ?, EmailUsuarioReservado = ?  WHERE ID = ?";

            // Preparar la sentencia con los valores
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, libro.getTitulo());
                pstmt.setString(2, libro.getAutor());
                pstmt.setString(3, libro.getAñoPublicacion());
                pstmt.setInt(4, libro.getDisponible() ? 1 : 0);
                pstmt.setString(5, libro.getFechaPrestamo());
                pstmt.setString(6, libro.getFechaDevolucion());
                pstmt.setString(7, libro.getEmailUsuarioReservado());
                pstmt.setInt(8, libro.getId());

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
    public boolean eliminarLibro(Libro libro) {
        return false;
    }

    @Override
    public ArrayList<Libro> obtenerTodosLibros() {

        ArrayList<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM LIBROS";
        String url = "jdbc:sqlite:data/bbdd_practica1.db";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setTitulo(rs.getString("Titulo"));
                libro.setAutor(rs.getString("Autor"));
                libro.setAñoPublicacion(rs.getString("AñoPublicacion"));
                libro.setDisponible(rs.getInt("Disponible") == 1);
                libro.setFechaPrestamo(rs.getString("FechaPrestamo"));
                libro.setFechaDevolucion(rs.getString("FechaDevolucion"));
                libro.setEmailUsuarioReservado(rs.getString("EmailUsuarioReservado"));
                libro.setId(rs.getInt("ID"));
                libros.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;

    }
}
