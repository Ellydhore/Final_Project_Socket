package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.handler.AuthenticationHandler;
import com.example.final_project_socket.handler.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    @FXML
    private Button btn_submit, btn_signup;
    @FXML
    private TextField txtf_username;
    @FXML
    private PasswordField passf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_submit.setOnAction(event -> AuthenticationHandler.signInUser(event, txtf_username.getText(), passf_password.getText()));
        btn_signup.setOnAction(event -> SceneHandler.changeScene(event, "/com/example/final_project_socket/fxml/Sign_Up.fxml", "Sign Up!", null, null));
    }
}
