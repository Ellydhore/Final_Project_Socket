package com.example.final_project_socket.handler;

import com.example.final_project_socket.fxml_controller.ChatBoxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.Socket;

/*
    #########################################################################################################

    This class manages the switching of scenes between the Sign-Up, Sign-In, and Chat-Box FXML files.
    (See how: https://youtu.be/ltX5AtW9v30?t=2128)

    #########################################################################################################
*/

public class SceneHandler {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, Socket socket) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(AuthenticationHandler.class.getResource(fxmlFile));
            root = loader.load();

            if (username != null) {
                ChatBoxController chatBoxController = loader.getController();
                chatBoxController.setUserInformation(username);
                chatBoxController.setSocket(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (root != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

}
