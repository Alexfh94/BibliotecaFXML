package com.carballeira.practica1.model;

public class Libro {

    private String titulo;
    private String autor;
    private String añoPublicacion;
    private Boolean disponible;
    private String fechaDevolucion;
    private String emailUsuarioReservado;

    public Libro(String titulo, String autor, String añoPublicacion, Boolean disponible, String fechaDevolucion, String emailUsuarioReservado) {
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.disponible = disponible;
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

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEmailUsuarioReservado() {
        return emailUsuarioReservado;
    }

    public void setEmailUsuarioReservado(String emailUsuarioReservado) {
        this.emailUsuarioReservado = emailUsuarioReservado;
    }

    public boolean isDisponible(){

        if (disponible=true)
            return true;
        else
            return false;

    }
}
