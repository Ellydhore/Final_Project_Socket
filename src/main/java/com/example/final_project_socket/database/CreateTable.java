package com.example.final_project_socket.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void createTable() {
        try(Connection connection = MySQLConnector.getConnection();
            Statement statement = connection.createStatement()) {
            String query1 = "CREATE TABLE IF NOT EXISTS tblusers (" +
                    "userid INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL," +
                    "is_online TINYINT(1) DEFAULT 0 )";
            statement.execute(query1);

            String query2 = "CREATE TABLE IF NOT EXISTS tblmessages (" +
                    "messageid INT AUTO_INCREMENT PRIMARY KEY," +
                    "userid INT," +
                    "FOREIGN KEY (userid) REFERENCES tblusers(userid)," +
                    "message VARCHAR(255) NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(query2);

            String query3 = "CREATE TABLE IF NOT EXISTS tblprofile (" +
                    "profileid INT AUTO_INCREMENT PRIMARY KEY," +
                    "userid INT," +
                    "FOREIGN KEY (userid) REFERENCES tblusers(userid)," +
                    "highscore INT(255) DEFAULT 0," +
                    "profileimage TINYINT(255) DEFAULT 0," +
                    "datejoined TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(query3);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}