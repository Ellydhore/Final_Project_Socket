package com.example.final_project_socket.socket;

import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    // Using a thread safe array list
    public static CopyOnWriteArrayList<com.example.final_project_socket.socket.ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // After the username has been initialized, it must be written to the server right away
            // because the username is the key to informing the ClientHandler that the FIRST message
            // sent SHOULD be a username.
            // (See link: https://youtu.be/gLfuZrrfKes?t=911).
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);

            broadcastMessage("Server: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        try {
            // Listens for incoming messages.
            while ((messageFromClient = bufferedReader.readLine()) != null && !socket.isClosed()) {
                if(messageFromClient.equals("--DISCONNECTED--")) {
                    closeEverything();
                    break;
                }
                broadcastMessage(messageFromClient);
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (com.example.final_project_socket.socket.ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.equals(this)) {
                try {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                } catch (IOException e) {
                    clientHandler.closeEverything();
                }
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

    public void closeEverything() {
        removeClientHandler();
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null && !socket.isClosed()) {
                socket.close();
            }
            broadcastMessage("Server: " + clientUsername + " has left the chat!");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}