package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.utility.DBUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button btn_submit;
    @FXML
    private Button btn_signin;
    @FXML
    private TextField txtf_username;
    @FXML
    private PasswordField passf_password;
    @FXML
    private PasswordField passf_rpassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Check if all credential are all filled in
                if(!txtf_username.getText().trim().isEmpty() &&
                   !passf_password.getText().trim().isEmpty() &&
                   !passf_rpassword.getText().trim().isEmpty()) {
                    // Check if password and repeat password are matched
                    if(!passf_password.getText().equals(passf_rpassword.getText())) {
                        System.out.println("Password does not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Password mismatch!");
                        alert.show();
                    } else {
                        DBUtil.signUpUser(event, txtf_username.getText(), passf_password.getText());
                    }
                } else {
                    System.out.println("Please fill in all credentials!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all credentials.");
                    alert.show();
                }
            }
        });

        btn_signin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtil.changeScene(event, "/com/example/final_project_socket/fxml/Sign_In.fxml", "Sign In!", null);
            }
        });
    }
}