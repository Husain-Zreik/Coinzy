package Coinzy.providers;

import Coinzy.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProvider {
    private static final Logger logger = Logger.getLogger(UserProvider.class.getName());

    public boolean isValidUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Return true if user exists
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during login validation", e);
        }
        return false; // Return false if an error occurs or user doesn't exist
    }

    public int getUserId(String username) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT id FROM users WHERE username=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching user ID", e);
        }
        return -1; // Return -1 if user not found or error occurs
    }
}
