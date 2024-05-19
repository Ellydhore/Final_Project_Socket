package com.example.final_project_socket.handler;

import com.example.final_project_socket.database.MySQLConnector;
import com.example.final_project_socket.database.Queries;
import com.example.final_project_socket.socket.Client;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

/*
    #########################################################################################################

    This class manages the logic for database queries.
    (See how: https://youtu.be/ltX5AtW9v30?t=2128)

    #########################################################################################################
*/

public class AuthenticationHandler {
    private static Socket socket;
    private static Client client;

    public static void signUpUser(ActionEvent event, String username, String password) {
        AlertHandler alert = new AlertHandler();
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM tblusers WHERE username = ?");
             PreparedStatement psInsertUser = connection.prepareStatement("INSERT INTO tblusers (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psInsertProfile = connection.prepareStatement("INSERT INTO tblprofile (userid) VALUES (?)")) {

            // Check if the username already exists
            psCheckUserExists.setString(1, username);
            try (ResultSet resultSet = psCheckUserExists.executeQuery()) {
                if (resultSet.next()) {
                    alert.error("Sign up attempt", "User already exists!");
                    return;
                }
            }

            psInsertUser.setString(1, username);
            psInsertUser.setString(2, password);
            psInsertUser.executeUpdate();

            try (ResultSet generatedKeys = psInsertUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    psInsertProfile.setInt(1, userId);
                    psInsertProfile.executeUpdate();
                    alert.information("Success", "You have successfully signed up!");
                    SceneHandler.changeScene(event, "/com/example/final_project_socket/fxml/Sign_In.fxml", "Sign In!", null, null, null);
                } else {
                    throw new SQLException("Failed to get generated keys, no userid obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert.error("SQLException", "An error occurred. Please try again.");
        }
    }

    public static void signInUser(ActionEvent event, String username, String password) {
        AlertHandler alert = new AlertHandler();
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT password, userid , is_online FROM tblusers WHERE username = ?")) {
            ps.setString(1, username);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (!resultSet.next()) {
                    alert.error("Failed to sign in", "Provided credentials are incorrect!");
                    return;
                }

                String retrievedPassword = resultSet.getString("password");
                int userId = resultSet.getInt("userid");
                if (resultSet.getBoolean("is_online")) {
                    alert.error("Failed to sign in", "User is already signed in!");
                    return;
                }

                if (retrievedPassword.equals(password)) {
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE tblusers SET is_online = ? WHERE userid = ?")) {
                        statement.setBoolean(1, true);
                        statement.setInt(2, userId);
                        statement.executeUpdate();
                    }
                    socket = new Socket("localhost", 9806);
                    client = new Client(socket, username);

                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        if (!socket.isClosed()) {
                            disconnect(username);
                        }
                    }));

                    new Thread(() -> {
                        while(!socket.isClosed()) {
                            client.listenForMessage();
                        }
                    }).start();

                    SceneHandler.changeScene(event, "/com/example/final_project_socket/fxml/Home.fxml", "Welcome!", username, socket, client);
                } else {
                    alert.error("Failed to sign in", "Provided credentials are incorrect!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert.error("SQLException", "An error occurred. Please try again.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnect(String username) {
        try {
            Queries.updateIsOnline(false, username);
            client.sendMessage("--DISCONNECTED--");
            socket.close();
            System.out.println("[User disconnected]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}