package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.TargetAmount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TargetAmountProvider {
    private static final Logger logger = Logger.getLogger(TargetAmountProvider.class.getName());

    public TargetAmount getTargetAmountById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM target_amounts WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        TargetAmount targetAmount = new TargetAmount();
                        targetAmount.setId(rs.getInt("id"));
                        targetAmount.setUserId(rs.getInt("user_id"));
                        targetAmount.setAmount(rs.getDouble("amount"));
                        return targetAmount;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching target amount by ID", e);
        }
        return null; // Return null if target amount not found or error occurs
    }

    public boolean createTargetAmount(TargetAmount targetAmount) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO target_amounts(user_id, amount) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, targetAmount.getUserId());
                stmt.setDouble(2, targetAmount.getAmount());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during target amount creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateTargetAmount, deleteTargetAmount)
}
