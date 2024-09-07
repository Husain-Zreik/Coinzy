package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Income;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncomeProvider {
    private static final Logger logger = Logger.getLogger(IncomeProvider.class.getName());

    // Create a new income
    public boolean createIncome(Income income) {
        String sql = "INSERT INTO incomes (user_id, category_id, amount, income_date, recurrence_pattern, note) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income.getUserId());
            pstmt.setInt(2, income.getCategoryId());
            pstmt.setBigDecimal(3, income.getAmount());
            pstmt.setDate(4, Date.valueOf(income.getIncomeDate())); // Convert LocalDate to java.sql.Date
            pstmt.setString(5, income.getRecurrencePattern());
            pstmt.setString(6, income.getNote());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during income creation", e);
        }
        return false;
    }

    // Get an income by ID
    public Income getIncomeById(int id) {
        String sql = "SELECT * FROM incomes WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToIncome(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching income by ID", e);
        }
        return null;
    }

    // Update an income
    public boolean updateIncome(Income income) {
        String sql = "UPDATE incomes SET user_id = ?, category_id = ?, amount = ?, income_date = ?, recurrence_pattern = ?, note = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, income.getUserId());
            pstmt.setInt(2, income.getCategoryId());
            pstmt.setBigDecimal(3, income.getAmount());
            pstmt.setDate(4, Date.valueOf(income.getIncomeDate())); // Convert LocalDate to java.sql.Date
            pstmt.setString(5, income.getRecurrencePattern());
            pstmt.setString(6, income.getNote());
            pstmt.setInt(7, income.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during income update", e);
        }
        return false;
    }

    // Delete an income
    public boolean deleteIncome(int id) {
        String sql = "DELETE FROM incomes WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during income deletion", e);
        }
        return false;
    }

    // Get all incomes for a user
    public List<Income> getIncomesByUserId(int userId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM incomes WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    incomes.add(mapRowToIncome(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching incomes by user ID", e);
        }
        return incomes;
    }

    // Map ResultSet to Income
    private Income mapRowToIncome(ResultSet rs) throws SQLException {
        return new Income(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("category_id"),
                rs.getBigDecimal("amount"),
                rs.getDate("income_date").toLocalDate(), // Convert java.sql.Date to LocalDate
                rs.getString("recurrence_pattern"),
                rs.getString("note"));
    }
}
