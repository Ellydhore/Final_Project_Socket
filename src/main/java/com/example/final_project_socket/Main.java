package com.example.final_project_socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/*
    #########################################################################################################

    Go to readme.txt found in src/main/java/com/example/socket_final_project/readme.txt for what's happening.

    NOTE!
    -Requires the MySQL connector dependency, see DATABASE section in readme.

    #########################################################################################################
*/

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Sign_Up.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Socket");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}