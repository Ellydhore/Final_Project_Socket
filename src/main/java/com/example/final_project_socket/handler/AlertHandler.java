package com.example.final_project_socket.handler;

import javafx.scene.control.Alert;

public class AlertHandler {
    public void error(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
