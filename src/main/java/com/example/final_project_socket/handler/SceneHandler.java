package com.example.final_project_socket.handler;

import com.example.final_project_socket.fxml_controller.ChatBoxController;
import com.example.final_project_socket.fxml_controller.HomeController;
import com.example.final_project_socket.fxml_controller.ProfileController;
import com.example.final_project_socket.socket.Client;
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
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, Socket socket, Client client) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(AuthenticationHandler.class.getResource(fxmlFile));
            root = loader.load();
            if (username != null) {
                switch (fxmlFile) {
                    case "/com/example/final_project_socket/fxml/Chat_Box.fxml" -> {
                        ChatBoxController chatBoxController = loader.getController();
                        chatBoxController.setUserInformation(username, socket, client);
                    }
                    case "/com/example/final_project_socket/fxml/Profile.fxml" -> {
                        ProfileController profileController = loader.getController();
                        profileController.setUserInformation(username, socket, client);
                    }
                    case "/com/example/final_project_socket/fxml/Home.fxml" -> {
                        HomeController homeController = loader.getController();
                        homeController.setUserInformation(username, socket, client);
                    }
                }
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