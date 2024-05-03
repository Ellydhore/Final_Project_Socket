package com.example.final_project_socket.utility;

import javafx.event.ActionEvent;
import java.sql.*;

/*
    #########################################################################################################

    This class manages the logic for database queries.
    (See how: https://youtu.be/ltX5AtW9v30?t=2128)

    #########################################################################################################
*/

public class DBUtil {
    public static void signUpUser(ActionEvent event, String username, String password) {
        AlertUtil alert = new AlertUtil();
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM tblusers WHERE username = ?");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO tblusers (username, password) VALUES (?, ?)")) {

            psCheckUserExists.setString(1, username);

            try (ResultSet resultSet = psCheckUserExists.executeQuery()) {
                if (resultSet.next()) {
                    alert.error("User already exists!");
                    return;
                }
            }

            psInsert.setString(1, username);
            psInsert.setString(2, password);
            psInsert.executeUpdate();

            SceneUtil.changeScene(event, "/com/example/final_project_socket/fxml/Chat_Box.fxml", "Welcome!", username);
        } catch (SQLException e) {
            e.printStackTrace();
            alert.error("An error occurred. Please try again.");
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        AlertUtil alert = new AlertUtil();
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT password, userid FROM tblusers WHERE username = ?")) {

            ps.setString(1, username);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (!resultSet.next()) {
                    alert.error("Provided credentials are incorrect!");
                    return;
                }

                String retrievedPassword = resultSet.getString("password");
                int userId = resultSet.getInt("userid");
                if (retrievedPassword.equals(password)) {
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE tblusers SET is_online = ? WHERE userid = ?")) {
                        statement.setBoolean(1, true);
                        statement.setInt(2, userId);
                        statement.executeUpdate();
                    }
                    SceneUtil.changeScene(event, "/com/example/final_project_socket/fxml/Chat_Box.fxml", "Welcome!", username);
                } else {
                    alert.error("Provided credentials are incorrect!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            alert.error("An error occurred. Please try again.");
        }
    }
}
