package com.carballeira.practica1.model;

import static com.carballeira.practica1.utils.AlertUtils.showErrorAlert;

public class Usuario {

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
        //TODO DESCOMENTAR TRAS LAS PRUEBAS
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
}
