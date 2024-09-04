package Coinzy.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/coinzy_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect(); // Attempt to connect if connection is null or closed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to the database");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            if (connection == null || connection.isClosed()) {
                connect(); // Ensure connection is established before executing query
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the Statement after the ResultSet is fully used
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultSet;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
