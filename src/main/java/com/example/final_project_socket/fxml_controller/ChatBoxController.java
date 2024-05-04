package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.socket.Client;
import com.example.final_project_socket.utility.MySQLConnector;
import com.example.final_project_socket.utility.SceneUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private Button btn_disconnect, btn_send;
    @FXML
    private Text txt_name;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private VBox vb_messages;
    @FXML
    private TextField txtf_sendmsgbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket socket = new Socket("localhost", 9806);
            // runLater() ensures that txt_name.getText() retrieves the value from the FXML.
            // (See link: https://stackoverflow.com/questions/68363535/passing-data-to-another-controller-in-javafx).
            Platform.runLater(() -> {
                Parent root = btn_disconnect.getParent();
                Stage stage = (Stage) root.getScene().getWindow();

                Client client = new Client(socket, txt_name.getText());
                client.listenForMessage(vb_messages);

                btn_send.setOnAction(actionEvent -> {
                    String messageToSend = txtf_sendmsgbox.getText();
                    // This code wraps any sent messages in a nice bubble.
                    if (!messageToSend.isEmpty()) {
                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                        hBox.setPadding(new Insets(5, 10, 5, 150));

                        Text text = new Text(messageToSend);

                        TextFlow textFlow = new TextFlow(text);
                        textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
                                "-fx-background-color: rgb(3, 172, 19);" +
                                "-fx-background-radius: 20px;" +
                                "-fx-font-size: 20px");
                        textFlow.setPadding(new Insets(5, 10, 5, 10));
                        text.setFill(Color.color(0.934, 0.945, 0.996));
                        hBox.getChildren().add(textFlow);
                        vb_messages.getChildren().add(hBox);

                        client.sendMessage(messageToSend);
                        txtf_sendmsgbox.clear();
                    }
                });

                btn_disconnect.setOnAction(actionEvent -> {
                    disconnect(client, socket);
                    SceneUtil.changeScene(actionEvent, "/com/example/final_project_socket/fxml/Sign_In.fxml", "Log in", null);
                });


                stage.setOnCloseRequest(actionEvent -> {
                    disconnect(client, socket);
                    actionEvent.consume();
                    stage.close();
                });
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // This code dynamically changes the size of the message box
        vb_messages.heightProperty().addListener(
                (observableValue, oldValue, newValue) -> sp_main.setVvalue((Double) newValue));
    }

    // This method wraps any received messages in a nice bubble.
    public static void addLabel(String messageReceived, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 150 ,5, 10));

        Text text = new Text(messageReceived);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(178, 211, 194);" +
                "-fx-background-radius: 20px;" +
                "-fx-font-size: 20px");
        textFlow.setPadding(new Insets(5, 5, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

    public void setUserInformation(String username) {
        txt_name.setText(username);
    }

    private void disconnect(Client client, Socket socket) {
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE tblusers SET is_online = ? WHERE username = ?")) {
            statement.setBoolean(1, false);
            statement.setString(2, txt_name.getText());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User disconnected successfully.");
            } else {
                System.out.println("User not found or already disconnected.");
            }
            // Using '--DISCONNECTED--' as a sentinel value informs the server that the client has disconnected.
            client.sendMessage("--DISCONNECTED--");
            socket.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}