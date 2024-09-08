package Coinzy.providers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Coinzy.database.DatabaseManager;
import Coinzy.models.User;

public class UserProvider {
    private static final Logger logger = Logger.getLogger(UserProvider.class.getName());
    private static final String SALT = generateSalt(); // Generate a salt once and use it for all hashes

    // Create a new user
    public boolean createUser(User user) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (name, email, username, password, role_id, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, hashPassword(user.getPassword())); // Hash password before storing
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

    // Validate user credentials
    public boolean isValidUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        return hashPassword(password).equals(hashedPassword); // Check if the provided password matches
                                                                              // the hashed password
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during login validation", e);
        }
        return false;
    }

    // Hash the password using SHA-256 with salt
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(SALT.getBytes()); // Add salt to the hash
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Generate a salt
    private static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
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

    // Fetch users by status (Pending, Accepted, Declined)
    public List<User> getUsersByStatus(String status) {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE status = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(mapRowToUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching users by status", e);
        }
        return users;
    }

    public boolean updateStatus(int userId, String newStatus) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE users SET status = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newStatus);
                pstmt.setInt(2, userId);

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user status update", e);
        }
        return false;
    }

    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        String searchQuery = "%" + query.toLowerCase() + "%";
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT u.* FROM users u " +
                    "JOIN roles r ON u.role_id = r.id " +
                    "WHERE u.id = ? OR LOWER(u.username) LIKE ? OR LOWER(u.email) LIKE ? " +
                    "OR LOWER(u.status) LIKE ? OR LOWER(r.role_name) LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // For ID, we directly check if the query is a number, otherwise, we treat it as
                // text for other fields
                try {
                    pstmt.setInt(1, Integer.parseInt(query));
                } catch (NumberFormatException e) {
                    pstmt.setInt(1, -1); // If not a valid number, set to -1 (assuming there's no user with this ID)
                }
                pstmt.setString(2, searchQuery); // username
                pstmt.setString(3, searchQuery); // email
                pstmt.setString(4, searchQuery); // status
                pstmt.setString(5, searchQuery); // role_name

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(mapRowToUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user search", e);
        }
        return users;
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
