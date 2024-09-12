package Coinzy.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import Coinzy.database.DatabaseManager;
import Coinzy.models.User;

public class UserProvider {
    private static final Logger logger = Logger.getLogger(UserProvider.class.getName());

    // Hash a password using bcrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Check if a password is valid
    private boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    public boolean createUser(User user) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (name, email, username, password, role_id, status, owner_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, hashPassword(user.getPassword())); // Hash the password
                pstmt.setInt(5, user.getRoleId());
                pstmt.setString(6, user.getStatus());
                pstmt.setObject(7, user.getOwnerId(), Types.INTEGER);
                pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Set the current time if null

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user creation", e);
        }
        return false;
    }

    // Update a user with created_at attribute
    public boolean updateUser(User user) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE users SET name = ?, email = ?, username = ?, password = ?, role_id = ?, status = ?, owner_id = ?, created_at = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getEmail());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, hashPassword(user.getPassword())); // Hash the password
                pstmt.setInt(5, user.getRoleId());
                pstmt.setString(6, user.getStatus());
                pstmt.setObject(7, user.getOwnerId(), Types.INTEGER);
                pstmt.setTimestamp(8, user.getCreatedAt()); // Update created_at timestamp
                pstmt.setInt(9, user.getId());

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user update", e);
        }
        return false;
    }

    // Check if username and password are valid
    public boolean isValidUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("password");
                        return checkPassword(password, hashedPassword); // Check hashed password
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during login validation", e);
        }
        return false;
    }

    public User getUser(String identifier) {
        String sql;
        boolean isNumeric = identifier.chars().allMatch(Character::isDigit);

        if (isNumeric) {
            sql = "SELECT * FROM users WHERE id = ?";
        } else if (identifier.contains("@")) {
            sql = "SELECT * FROM users WHERE email = ?";
        } else {
            sql = "SELECT * FROM users WHERE username = ?";
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, identifier);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching user", e);
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

    public int getTotalUsersCount() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT COUNT(*) AS total FROM users";
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching total users", e);
        }
        return 0;
    }

    // Get count of users by status (active, pending, etc.)
    public int getUsersByStatusCount(String status) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT COUNT(*) AS total FROM users WHERE status = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("total");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching users by status", e);
        }
        return 0;
    }

    public Map<String, Integer> getUserGrowthByMonth() {
        Map<String, Integer> userGrowth = new LinkedHashMap<>(); // LinkedHashMap maintains insertion order

        // Get the current year
        int currentYear = LocalDate.now().getYear();

        // SQL query to retrieve user counts by month for the current year
        String query = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS month, COUNT(*) AS user_count " +
                "FROM users " +
                "WHERE YEAR(created_at) = ? " +
                "GROUP BY month " +
                "ORDER BY month ASC";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the current year in the query
            stmt.setInt(1, currentYear);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String month = rs.getString("month");
                    int count = rs.getInt("user_count");
                    userGrowth.put(month, count);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching user growth", e);
        }

        return userGrowth;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("role_id"),
                rs.getString("status"),
                (Integer) rs.getObject("owner_id"),
                rs.getTimestamp("created_at")); // Ensure created_at is treated as a Timestamp
    }
}
