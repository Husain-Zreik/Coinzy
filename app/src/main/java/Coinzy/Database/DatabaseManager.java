package Coinzy.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private static Connection connection;

    private static String url;
    private static String user;
    private static String password;

    static {
        // Load properties from the configuration file
        Properties properties = new Properties();
        try (InputStream input = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("config/database.properties")) {
            if (input == null) {
                logger.severe("Sorry, unable to find database.properties");
            } else {
                properties.load(input);
                url = properties.getProperty("db.url");
                user = properties.getProperty("db.username");
                password = properties.getProperty("db.password");

                // Load the driver class if specified
                String driverClassName = properties.getProperty("db.driverClassName");
                if (driverClassName != null && !driverClassName.isEmpty()) {
                    Class.forName(driverClassName);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Error loading database properties", ex);
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.info("Connection is null or closed. Attempting to connect.");
                connect(); // Attempt to connect if connection is null or closed
            } else {
                logger.info("Connection is already established.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting connection", e);
        }
        return connection;
    }

    public static void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.info("Establishing a new database connection.");
                connection = DriverManager.getConnection(url, user, password);
                logger.log(Level.INFO, "Connected to the database at {0}", System.currentTimeMillis());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to the database", e);
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
            logger.log(Level.SEVERE, "Error executing query", e);
        } finally {
            // Close the Statement after the ResultSet is fully used
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing statement", e);
            }
        }
        return resultSet;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing the database connection", e);
        }
    }
}
