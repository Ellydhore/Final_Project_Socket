package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.utility.DBUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    @FXML
    private Button btn_connect;
    @FXML
    private Button btn_signup;
    @FXML
    private TextField txtf_username;
    @FXML
    private PasswordField passf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtil.logInUser(event, txtf_username.getText(), passf_password.getText());
            }
        });

        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtil.changeScene(event, "/com/example/final_project_socket/fxml/Sign_Up.fxml", "Sign Up!", null);
            }
        });
    }
}
