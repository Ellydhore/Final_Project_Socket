package com.example.final_project_socket.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            Socket socket;
            while(!serverSocket.isClosed()) {
                socket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
                System.out.println("Server: A new client has connected!");
            }
        } catch(IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null) {
                serverSocket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Waiting for clients to join");
        ServerSocket serverSocket = new ServerSocket(9806);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
