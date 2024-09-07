package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageProvider {
    private static final Logger logger = Logger.getLogger(MessageProvider.class.getName());

    // Create a new message
    public boolean createMessage(Message message) {
        String sql = "INSERT INTO messages (chat_id, sender_id, message_text, message_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, message.getChatId());
            pstmt.setInt(2, message.getSenderId());
            pstmt.setString(3, message.getMessage());
            pstmt.setTimestamp(4, message.getTimestamp());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during message creation", e);
        }
        return false;
    }

    // Get a message by ID
    public Message getMessageById(int id) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMessage(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching message by ID", e);
        }
        return null;
    }

    // Update a message
    public boolean updateMessage(Message message) {
        String sql = "UPDATE messages SET chat_id = ?, sender_id = ?, message_text = ?, message_date = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, message.getChatId());
            pstmt.setInt(2, message.getSenderId());
            pstmt.setString(3, message.getMessage());
            pstmt.setTimestamp(4, message.getTimestamp());
            pstmt.setInt(5, message.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during message update", e);
        }
        return false;
    }

    // Delete a message
    public boolean deleteMessage(int id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during message deletion", e);
        }
        return false;
    }

    // Get all messages for a chat
    public List<Message> getMessagesByChatId(int chatId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE chat_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chatId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(mapRowToMessage(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching messages by chat ID", e);
        }
        return messages;
    }

    // Map ResultSet to Message
    private Message mapRowToMessage(ResultSet rs) throws SQLException {
        return new Message(
                rs.getInt("id"),
                rs.getInt("chat_id"),
                rs.getInt("sender_id"),
                rs.getString("message_text"),
                rs.getTimestamp("message_date"));
    }
}
