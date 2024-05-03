package com.example.final_project_socket.utility;

import javafx.scene.control.Alert;

public class AlertUtil {
    public void error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
