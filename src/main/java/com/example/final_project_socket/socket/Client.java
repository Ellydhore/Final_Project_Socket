package com.example.final_project_socket.socket;

import java.io.*;
import java.net.Socket;
import com.example.final_project_socket.fxml_controller.ChatBoxController;
import javafx.scene.layout.VBox;

/*
    #########################################################################################################

    This class is used by the ChatBoxController, and it handles the connection to the server and
    sends data to be processed.

    Using '--DISCONNECTED--' as a sentinel value informs the server that the client has disconnected.

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
            // because the ClientHandler requires a username as its first message.
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
            System.out.println("Client closed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}