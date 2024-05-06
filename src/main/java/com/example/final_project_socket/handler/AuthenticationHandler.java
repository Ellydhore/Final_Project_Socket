package com.example.final_project_socket.handler;

import com.example.final_project_socket.database.MySQLConnector;
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
    public static void signUpUser(ActionEvent event, String username, String password) {
        AlertHandler alert = new AlertHandler();
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM tblusers WHERE username = ?");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO tblusers (username, password) VALUES (?, ?)")) {
            psCheckUserExists.setString(1, username);

            try (ResultSet resultSet = psCheckUserExists.executeQuery()) {
                if (resultSet.next()) {
                    alert.error("Sign up attempt", "User already exists!");
                    return;
                }
            }

            psInsert.setString(1, username);
            psInsert.setString(2, password);
            psInsert.executeUpdate();

            SceneHandler.changeScene(event, "/com/example/final_project_socket/fxml/Chat_Box.fxml", "Welcome!", username, null);
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

                Socket socket = new Socket("localhost", 9806);
                if (retrievedPassword.equals(password)) {
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE tblusers SET is_online = ? WHERE userid = ?")) {
                        statement.setBoolean(1, true);
                        statement.setInt(2, userId);
                        statement.executeUpdate();
                    }
                    SceneHandler.changeScene(event, "/com/example/final_project_socket/fxml/Chat_Box.fxml", "Welcome!", username, socket);
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
}