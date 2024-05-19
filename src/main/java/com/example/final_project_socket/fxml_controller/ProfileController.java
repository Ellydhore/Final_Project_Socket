package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.database.Queries;
import com.example.final_project_socket.handler.AlertHandler;
import com.example.final_project_socket.handler.AuthenticationHandler;
import com.example.final_project_socket.handler.SceneHandler;
import com.example.final_project_socket.socket.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button btn_disconnect, btn_return, btn_previous, btn_next, btn_cancel, btn_save;
    @FXML
    private ImageView img_profile, img_edit_profile;
    @FXML
    private Text txt_name, txt_name2;
    @FXML
    private PasswordField passf_password, passf_rpassword;
    private Client client;
    private Socket socket;
    private int currentProfileImageIndex, selectedProfileImageIndex;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AlertHandler alert = new AlertHandler();

        // runLater() ensures that txt_name.getText() retrieves the value from the FXML.
        // (See link: https://stackoverflow.com/questions/68363535/passing-data-to-another-controller-in-javafx).
        Platform.runLater(() -> {
            String username = txt_name.getText();
            Parent root = btn_disconnect.getParent();
            Stage stage = (Stage) root.getScene().getWindow();
            currentProfileImageIndex = Queries.getProfilePicture(username);
            selectedProfileImageIndex = Queries.getProfilePicture(username);

            loadImage(username);
            txt_name2.setText(username);

            btn_return.setOnAction(actionEvent -> {
                SceneHandler.changeScene(actionEvent, "/com/example/final_project_socket/fxml/Chat_Box.fxml", "Chat", txt_name.getText(), socket, client);
                txt_name.setText("");
            });

            btn_previous.setOnAction(actionEvent -> {
                selectedProfileImageIndex--;
                if(selectedProfileImageIndex < 0) {
                    selectedProfileImageIndex = 5;
                }
                img_edit_profile.setImage(Queries.setProfilePicture(selectedProfileImageIndex));
            });

            btn_next.setOnAction(actionEvent -> {
                selectedProfileImageIndex++;
                if(selectedProfileImageIndex > 5) {
                    selectedProfileImageIndex = 0;
                }
                img_edit_profile.setImage(Queries.setProfilePicture(selectedProfileImageIndex));
            });

            btn_save.setOnAction(actionEvent -> {
                if(currentProfileImageIndex != selectedProfileImageIndex && passf_password.getText().isEmpty() && passf_rpassword.getText().isEmpty()) {
                    Optional<ButtonType> result = alert.confirmation("Updating profile", "Are you sure you want to update your profile?");
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Queries.updateProfilePicture(username, selectedProfileImageIndex);
                        img_profile.setImage(Queries.setProfilePicture(selectedProfileImageIndex));
                        currentProfileImageIndex = selectedProfileImageIndex;
                        alert.information("Success", "You have successfully updated your profile!");
                        return;
                    }
                }

                if(currentProfileImageIndex == selectedProfileImageIndex && passf_password.getText().isEmpty() && passf_rpassword.getText().isEmpty()) {
                    alert.error("Edit profile", "Please enter a new password or select a new image");
                    return;
                }

                if(passf_password.getText().isEmpty() || passf_rpassword.getText().isEmpty()) {
                    alert.error("Edit profile", "Please fill in all fields");
                    return;
                }

                Optional<ButtonType> result = alert.confirmation("Updating profile", "Are you sure you want to update your profile?");
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if(!passf_password.getText().equals(passf_rpassword.getText())) {
                        alert.error("ERROR", "Password does not match!");
                        return;
                    }
                    if(passf_password.getText().equals(Queries.getPassword(username))) {
                        alert.error("ERROR", "New password matches the current one!");
                        return;
                    }
                    if(currentProfileImageIndex != selectedProfileImageIndex) {
                        Queries.updateProfilePicture(username, selectedProfileImageIndex);
                        img_profile.setImage(Queries.setProfilePicture(selectedProfileImageIndex));
                        currentProfileImageIndex = selectedProfileImageIndex;
                    }

                    Queries.updatePassword(username, passf_password.getText());
                    passf_password.clear();
                    passf_rpassword.clear();
                    alert.information("Success", "You have successfully updated your profile!");
                }
            });

            btn_cancel.setOnAction(actionEvent -> {
                selectedProfileImageIndex = Queries.getProfilePicture(username);
                img_edit_profile.setImage(Queries.setProfilePicture(selectedProfileImageIndex));
                passf_password.setText("");
                passf_rpassword.setText("");
            });

            btn_disconnect.setOnAction(actionEvent -> {
                AuthenticationHandler.disconnect(username);
                txt_name.setText("");
                SceneHandler.changeScene(actionEvent, "/com/example/final_project_socket/fxml/Sign_In.fxml", "Log in", null, null, null);
            });

            stage.setOnCloseRequest(actionEvent -> {
                if (!txt_name.getText().isEmpty()) {
                    AuthenticationHandler.disconnect(username);
                }
            });
        });

    }

    public void setUserInformation(String username, Socket socket, Client client) {
        txt_name.setText(username);
        this.socket = socket;
        this.client = client;
    }

    private void loadImage(String username) {
        int image = Queries.getProfilePicture(username);
        img_profile.setImage(Queries.setProfilePicture(image));
        img_edit_profile.setImage(Queries.setProfilePicture(image));
    }

    private void disconnect(Client client, Socket socket, int exitStatus) {
        Queries.updateIsOnline(false, txt_name.getText());
        try {
            client.sendMessage("--DISCONNECTED--");
            socket.close();
            System.out.println("User disconnected [Exit Condition " + exitStatus +" ]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
