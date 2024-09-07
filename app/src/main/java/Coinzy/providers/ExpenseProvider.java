package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseProvider {
    private static final Logger logger = Logger.getLogger(ExpenseProvider.class.getName());

    public Expense getExpenseById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM expenses WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Expense expense = new Expense();
                        expense.setId(rs.getInt("id"));
                        expense.setUserId(rs.getInt("user_id"));
                        expense.setAccountId(rs.getInt("account_id"));
                        expense.setExpenseDate(rs.getDate("expense_date"));
                        expense.setExpenseCategory(rs.getInt("expense_category"));
                        expense.setRemark(rs.getString("remark"));
                        expense.setAmount(rs.getDouble("amount"));
                        return expense;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching expense by ID", e);
        }
        return null; // Return null if expense not found or error occurs
    }

    public boolean createExpense(Expense expense) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO expenses(user_id, account_id, expense_date, expense_category, remark, amount) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, expense.getUserId());
                stmt.setInt(2, expense.getAccountId());
                stmt.setDate(3, expense.getExpenseDate());
                stmt.setInt(4, expense.getExpenseCategory());
                stmt.setString(5, expense.getRemark());
                stmt.setDouble(6, expense.getAmount());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during expense creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateExpense, deleteExpense)
}
