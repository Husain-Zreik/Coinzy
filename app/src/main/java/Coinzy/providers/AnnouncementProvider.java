package Coinzy.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Announcement;

public class AnnouncementProvider {
    private static final Logger logger = Logger.getLogger(AnnouncementProvider.class.getName());

    // Insert a new announcement into the database
    public boolean insertAnnouncement(Announcement announcement) {
        String sql = "INSERT INTO announcements (title, message, created_by) VALUES (?, ?, 1)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, announcement.getTitle());
            pstmt.setString(2, announcement.getMessage());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during user creation", e);
            return false;
        }
    }

    public List<Announcement> getRecentAnnouncements(int days) {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT a.id, a.title, a.message, u.name AS created_by, a.timestamp " +
                "FROM announcements a " +
                "LEFT JOIN users u ON a.created_by = u.id " +
                "WHERE a.timestamp >= NOW() - INTERVAL ? DAY";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, days);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(rs.getInt("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setMessage(rs.getString("message"));
                announcement.setCreatedBy(rs.getString("created_by"));
                announcement.setTimestamp(rs.getTimestamp("timestamp"));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred while fetching recent announcements", e);
        }
        return announcements;
    }

    public List<Announcement> getAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT a.id, a.title, a.message, u.name AS created_by, a.timestamp " +
                "FROM announcements a " +
                "LEFT JOIN users u ON a.created_by = u.id";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Announcement announcement = new Announcement();
                announcement.setId(rs.getInt("id"));
                announcement.setTitle(rs.getString("title"));
                announcement.setMessage(rs.getString("message"));
                announcement.setCreatedBy(rs.getString("created_by")); // Set the user's name instead of ID
                announcement.setTimestamp(rs.getTimestamp("timestamp"));
                announcements.add(announcement);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during fetching announcements", e);
        }
        return announcements;
    }

}
