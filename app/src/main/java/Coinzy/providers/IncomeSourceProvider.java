package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.IncomeSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncomeSourceProvider {
    private static final Logger logger = Logger.getLogger(IncomeSourceProvider.class.getName());

    public IncomeSource getIncomeSourceById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM income_sources WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        IncomeSource incomeSource = new IncomeSource();
                        incomeSource.setId(rs.getInt("id"));
                        incomeSource.setUserId(rs.getInt("user_id"));
                        incomeSource.setSourceName(rs.getString("source_name"));
                        return incomeSource;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching income source by ID", e);
        }
        return null; // Return null if income source not found or error occurs
    }

    public boolean createIncomeSource(IncomeSource incomeSource) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO income_sources(user_id, source_name) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, incomeSource.getUserId());
                stmt.setString(2, incomeSource.getSourceName());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during income source creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateIncomeSource, deleteIncomeSource)
}
