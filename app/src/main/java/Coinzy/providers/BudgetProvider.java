package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Budget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BudgetProvider {
    private static final Logger logger = Logger.getLogger(BudgetProvider.class.getName());

    public Budget getBudgetById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM budgets WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Budget budget = new Budget();
                        budget.setId(rs.getInt("id"));
                        budget.setUserId(rs.getInt("user_id"));
                        budget.setExpenseCategory(rs.getInt("expense_category"));
                        budget.setAmount(rs.getDouble("amount"));
                        return budget;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching budget by ID", e);
        }
        return null; // Return null if budget not found or error occurs
    }

    public boolean createBudget(Budget budget) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO budgets(user_id, expense_category, amount) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, budget.getUserId());
                stmt.setInt(2, budget.getExpenseCategory());
                stmt.setDouble(3, budget.getAmount());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during budget creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateBudget, deleteBudget)
}
