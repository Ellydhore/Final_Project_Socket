package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.utility.DBUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private Button btn_disconnect;
    @FXML
    private Text txt_name;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_disconnect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtil.changeScene(actionEvent, "/com/example/final_project_socket/fxml/Sign_In.fxml", "Log in", null);
            }
        });
    }

    public void setUserInformation(String username) {
        txt_name.setText("Player name: " + username);
    }
}
