package com.example.final_project_socket.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Queries {
    public static void updateIsOnline(boolean setStatus, String username) {
        try (Connection connection = MySQLConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE tblusers SET is_online = ? WHERE username = ?")) {
            statement.setBoolean(1, setStatus);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
