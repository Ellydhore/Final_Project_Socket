package com.example.final_project_socket.socket;

import java.io.*;
import java.net.Socket;
import com.example.final_project_socket.fxml_controller.ChatBoxController;
import javafx.scene.layout.VBox;

/*
    #########################################################################################################

    This class is an extension of the ChatBoxController class, located in the ChatBoxController.java
    file at src/main/java/com/example/final_project_socket/fxml_controller/, and it handles the
    connection to the server and sends data to be processed.

    NOTE!
    -The Server and ClientHandler may be implemented to another project.

    #########################################################################################################
*/
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

            // After the username has been initialized, it must be written to the server right away
            // because the username is the key to informing the ClientHandler that the FIRST message
            // sent SHOULD be a username.
            // (See link: https://youtu.be/gLfuZrrfKes?t=911).
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e) {
            closeEverything();
        }
    }

    public void sendMessage(String messageToSend) {
        try {
            if(socket.isConnected() && !messageToSend.equals("--DISCONNECTED--")) {
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch(IOException e) {
            closeEverything();
        }
    }

    public void listenForMessage(VBox vBox) {
        new Thread(() -> {
            while(socket.isConnected()) {
                try {
                    String msgFromGroupChat = bufferedReader.readLine();
                    ChatBoxController.addLabel(msgFromGroupChat, vBox);
                } catch (IOException e) {
                    System.out.println("Something went wrong with the message receiver!");
                    closeEverything();
                    break;
                }
            }
        }).start();
    }

    public void closeEverything() {
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}