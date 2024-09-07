package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseProvider {
    private static final Logger logger = Logger.getLogger(ExpenseProvider.class.getName());

    // Create a new expense
    public boolean createExpense(Expense expense) {
        String sql = "INSERT INTO expenses (user_id, category_id, amount, expense_date, recurrence_pattern, note) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Start transaction

            pstmt.setInt(1, expense.getUserId());
            pstmt.setInt(2, expense.getCategoryId());
            pstmt.setBigDecimal(3, expense.getAmount());
            pstmt.setDate(4, Date.valueOf(expense.getExpenseDate())); // Convert LocalDate to java.sql.Date
            pstmt.setString(5, expense.getRecurrencePattern());
            pstmt.setString(6, expense.getNote());

            int rowsInserted = pstmt.executeUpdate();
            conn.commit(); // Commit transaction
            return rowsInserted > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating expense", e);
            return false;
        }
    }

    // Get an expense by ID
    public Expense getExpenseById(int id) {
        String sql = "SELECT * FROM expenses WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToExpense(rs);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching expense by ID", e);
        }
        return null;
    }

    // Update an existing expense
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET user_id = ?, category_id = ?, amount = ?, expense_date = ?, recurrence_pattern = ?, note = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Start transaction

            pstmt.setInt(1, expense.getUserId());
            pstmt.setInt(2, expense.getCategoryId());
            pstmt.setBigDecimal(3, expense.getAmount());
            pstmt.setDate(4, Date.valueOf(expense.getExpenseDate())); // Convert LocalDate to java.sql.Date
            pstmt.setString(5, expense.getRecurrencePattern());
            pstmt.setString(6, expense.getNote());
            pstmt.setInt(7, expense.getId());

            int rowsUpdated = pstmt.executeUpdate();
            conn.commit(); // Commit transaction
            return rowsUpdated > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating expense", e);
            return false;
        }
    }

    // Delete an expense by ID
    public boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting expense", e);
            return false;
        }
    }

    // Get all expenses for a specific user
    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapRowToExpense(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching expenses for user ID", e);
        }
        return expenses;
    }

    // Helper method to map ResultSet to an Expense object
    private Expense mapRowToExpense(ResultSet rs) throws SQLException {
        return new Expense(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("category_id"),
                rs.getBigDecimal("amount"),
                rs.getDate("expense_date").toLocalDate(), // Convert SQL Date to LocalDate
                rs.getString("recurrence_pattern"),
                rs.getString("note"));
    }
}
