package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Chat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatProvider {
    private static final Logger logger = Logger.getLogger(ChatProvider.class.getName());

    // Create a new chat
    public boolean createChat(Chat chat) {
        String sql = "INSERT INTO chats (owner_id, member_id, chat_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, chat.getOwnerId());
            pstmt.setInt(2, chat.getMemberId());
            pstmt.setDate(3, Date.valueOf(chat.getChatDate())); // Convert LocalDate to java.sql.Date

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during chat creation", e);
        }
        return false;
    }

    // Get a chat by ID
    public Chat getChatById(int id) {
        String sql = "SELECT * FROM chats WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToChat(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching chat by ID", e);
        }
        return null;
    }

    // Update a chat
    public boolean updateChat(Chat chat) {
        String sql = "UPDATE chats SET owner_id = ?, member_id = ?, chat_date = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, chat.getOwnerId());
            pstmt.setInt(2, chat.getMemberId());
            pstmt.setDate(3, Date.valueOf(chat.getChatDate())); // Convert LocalDate to java.sql.Date
            pstmt.setInt(4, chat.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during chat update", e);
        }
        return false;
    }

    // Delete a chat
    public boolean deleteChat(int id) {
        String sql = "DELETE FROM chats WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during chat deletion", e);
        }
        return false;
    }

    // Get all chats for a user (either as owner or member)
    public List<Chat> getChatsByUserId(int userId) {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM chats WHERE owner_id = ? OR member_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chats.add(mapRowToChat(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching chats by user ID", e);
        }
        return chats;
    }

    // Map ResultSet to Chat
    private Chat mapRowToChat(ResultSet rs) throws SQLException {
        return new Chat(
                rs.getInt("id"),
                rs.getInt("owner_id"),
                rs.getInt("member_id"),
                rs.getDate("chat_date").toLocalDate() // Convert java.sql.Date to LocalDate
        );
    }
}
