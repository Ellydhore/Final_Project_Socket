package com.example.final_project_socket.database;

import javafx.scene.image.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {
    public static void updateIsOnline(boolean setStatus, String username) {
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tblusers SET is_online = ? WHERE username = ?")) {
            preparedStatement.setBoolean(1, setStatus);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean updatePassword(String username, String password) {
        try(Connection connection = MySQLConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tblusers SET password = ? WHERE username = ?")) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPassword(String username) {
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM tblusers WHERE username = ?")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("password");
                } else {
                    throw new RuntimeException("Username not found: " + username);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve password for username: " + username, e);
        }
    }

    public static boolean updateProfilePicture(String username, int newIndex) {
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE tblprofile p JOIN tblusers u ON u.userid = p.userid " +
                             "SET p.profileimage = ? WHERE u.username = ?")) {
            preparedStatement.setInt(1, newIndex);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getProfilePicture(String username) {
        int image = 0;
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.profileimage FROM tblusers u " +
                     "JOIN tblprofile p ON u.userid = p.userid WHERE u.username = ?")) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    image = resultSet.getInt("profileimage");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Image setProfilePicture(int index) {
        Image[] PROFILE_PICTURES = {
                new Image("file:src/main/resources/com/example/final_project_socket/images/profile_icon.png"),
                new Image("file:src/main/resources/com/example/final_project_socket/images/diwata.png"),
                new Image("file:src/main/resources/com/example/final_project_socket/images/martinez.png"),
                new Image("file:src/main/resources/com/example/final_project_socket/images/nina_tucker.png"),
                new Image("file:src/main/resources/com/example/final_project_socket/images/rick_roll.png"),
                new Image("file:src/main/resources/com/example/final_project_socket/images/vegita.png")
        };
        if (index >= 0 && index < PROFILE_PICTURES.length) {
            return PROFILE_PICTURES[index];
        } else {
            return new Image("file:src/main/resources/com/example/final_project_socket/images/profile_icon.png");
        }
    }
}
