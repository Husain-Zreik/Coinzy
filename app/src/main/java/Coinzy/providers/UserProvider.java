package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProvider {
    private static final Logger logger = Logger.getLogger(UserProvider.class.getName());

    // Create a new user
    public boolean createUser(User user) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (name, email, username, password, role_id, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, user.getPassword());
                pstmt.setInt(5, user.getRoleId());
                pstmt.setString(6, user.getStatus());
                pstmt.setObject(7, user.getOwnerId(), Types.INTEGER);

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user creation", e);
        }
        return false;
    }

    // Get a user by ID
    public User getUserById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching user by ID", e);
        }
        return null;
    }

    // Update a user
    public boolean updateUser(User user) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE users SET name = ?, email = ?, username = ?, password = ?, role_id = ?, status = ?, owner_id = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, user.getPassword());
                pstmt.setInt(5, user.getRoleId());
                pstmt.setString(6, user.getStatus());
                pstmt.setObject(7, user.getOwnerId(), Types.INTEGER);
                pstmt.setInt(8, user.getId());

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user update", e);
        }
        return false;
    }

    // Delete a user
    public boolean deleteUser(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsDeleted = pstmt.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user deletion", e);
        }
        return false;
    }

    // Check if username and password are valid
    public boolean isValidUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next(); // Return true if user exists
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during login validation", e);
        }
        return false; // Return false if an error occurs or user doesn't exist
    }

    // Get user ID by username
    public int getUserId(String username) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
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

    // Check if username exists
    public boolean isUserExists(String username) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT 1 FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user existence check", e);
        }
        return false;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users";
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching all users", e);
        }
        return users;
    }

    // Map ResultSet to User
    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("role_id"),
                rs.getString("status"),
                (Integer) rs.getObject("owner_id"));
    }
}
