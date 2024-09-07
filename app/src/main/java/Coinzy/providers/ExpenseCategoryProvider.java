package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.ExpenseCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseCategoryProvider {
    private static final Logger logger = Logger.getLogger(ExpenseCategoryProvider.class.getName());

    public ExpenseCategory getExpenseCategoryById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM expense_categories WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ExpenseCategory category = new ExpenseCategory();
                        category.setId(rs.getInt("id"));
                        category.setUserId(rs.getInt("user_id"));
                        category.setCategoryName(rs.getString("category_name"));
                        return category;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching expense category by ID", e);
        }
        return null; // Return null if category not found or error occurs
    }

    public boolean createExpenseCategory(ExpenseCategory category) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO expense_categories(user_id, category_name) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, category.getUserId());
                stmt.setString(2, category.getCategoryName());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during expense category creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateExpenseCategory,
    // deleteExpenseCategory)
}
