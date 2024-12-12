package com.carballeira.practica1.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtils {

    // Método para mostrar una alerta de tipo ERROR
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para mostrar una alerta de tipo INFORMATION
    public static void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Nuevo metodo para mostrar una alerta de tipo CONFIRMATION
    public static void showConfirmationAlert(String title, String message, ConfirmationCallback callback) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                callback.onConfirmed();
            }
        });
    }

    // Interfaz de callback para manejar la confirmación
    public interface ConfirmationCallback {
        void onConfirmed();
    }
}
