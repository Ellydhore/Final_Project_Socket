package com.example.final_project_socket.fxml_controller;

import com.example.final_project_socket.database.Queries;
import com.example.final_project_socket.socket.Client;
import com.example.final_project_socket.database.MySQLConnector;
import com.example.final_project_socket.handler.SceneHandler;
import javafx.application.Platform;
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
import java.sql.*;
import java.util.*;

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
    private Socket socket;
    private Client client;
    private boolean disconnected;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disconnected = false;

        // runLater() ensures that txt_name.getText() retrieves the value from the FXML.
        // (See link: https://stackoverflow.com/questions/68363535/passing-data-to-another-controller-in-javafx).
        Platform.runLater(() -> {
            String username = txt_name.getText();
            loadMessages(username);
            Parent root = btn_disconnect.getParent();
            Stage stage = (Stage) root.getScene().getWindow();

            client = new Client(socket, username);
            client.listenForMessage(vb_messages);

            btn_send.setOnAction(actionEvent -> {
                String messageToSend = txtf_sendmsgbox.getText();
                if (!messageToSend.isEmpty()) {
                    addMessageBubble(messageToSend, false);
                    client.sendMessage(messageToSend);
                    txtf_sendmsgbox.clear();
                }
            });

            stage.setOnCloseRequest(actionEvent -> {
                if (!disconnected) {
                    disconnect(client, socket, 2);
                    stage.close();
                }
            });

            // This is a temporary solution for clean up
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (!disconnected) {
                    disconnect(client, socket, 3);
                }
            }));
        });

        // This code dynamically changes the size of the message box
        vb_messages.heightProperty().addListener(
                (observableValue, oldValue, newValue) -> sp_main.setVvalue((Double) newValue));

        btn_disconnect.setOnAction(actionEvent -> {
            if (!disconnected) {
                disconnect(client, socket, 1);
                txt_name.setText("");
                SceneHandler.changeScene(actionEvent, "/com/example/final_project_socket/fxml/Sign_In.fxml", "Log in", null, null);
            }
        });
    }

    public void setUserInformation(String username) {
        txt_name.setText(username);
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static void addReceivedMessage(String messageReceived, VBox vBox) {
        // This method wraps any received messages in a nice bubble.
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

    private void addMessageBubble(String message, boolean other) {
        HBox hBox = new HBox();
        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        if(!other) {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 10, 5, 150));
            textFlow.setStyle("-fx-color: rgb(239, 242, 255);" +
                    "-fx-background-color: rgb(3, 172, 19);" +
                    "-fx-background-radius: 20px;" +
                    "-fx-font-size: 20px");
            text.setFill(Color.color(0.934, 0.945, 0.996));
            textFlow.setPadding(new Insets(5, 10, 5, 5));
        } else {
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 150 ,5, 10));
            textFlow.setStyle("-fx-background-color: rgb(178, 211, 194);" +
                    "-fx-background-radius: 20px;" +
                    "-fx-font-size: 20px");
            textFlow.setPadding(new Insets(5, 5, 5, 10));
        }
        hBox.getChildren().add(textFlow);
        vb_messages.getChildren().add(hBox);
    }

    public void loadMessages(String name) {
        if (name != null && !name.isEmpty()) {
            try (Connection connection = MySQLConnector.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT m.*, u.username FROM tblmessages m LEFT JOIN tblusers u ON m.userid = u.userid ORDER BY m.created_at DESC LIMIT 20")) {
                ResultSet rs = statement.executeQuery();
                ArrayList<String> msg = new ArrayList<>();
                ArrayList<Boolean> who = new ArrayList<>();
                while (rs.next()) {
                    String username = rs.getString("username");
                    String message = rs.getString("message");
                    if(username.equals(name)) {
                        msg.add(message);
                        who.add(false);
                    } else {
                        msg.add(username + ": " + message);
                        who.add(true);
                    }
                }
                for(int i = who.size() - 1; i >= 0; i--) {
                    addMessageBubble(msg.get(i), who.get(i));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void disconnect(Client client, Socket socket, int exitStatus) {
        Queries.updateIsOnline(false, txt_name.getText());
        try {
            client.sendMessage("--DISCONNECTED--");
            socket.close();
            disconnected = true;
            System.out.println("User disconnected [Exit Condition " + exitStatus +" ]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}