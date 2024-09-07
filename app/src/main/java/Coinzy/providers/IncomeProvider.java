package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncomeProvider {
    private static final Logger logger = Logger.getLogger(IncomeProvider.class.getName());

    public Income getIncomeById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM incomes WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Income income = new Income();
                        income.setId(rs.getInt("id"));
                        income.setUserId(rs.getInt("user_id"));
                        income.setAccountId(rs.getInt("account_id"));
                        income.setIncomeDate(rs.getDate("income_date"));
                        income.setIncomeSource(rs.getString("income_source"));
                        income.setAmount(rs.getDouble("amount"));
                        return income;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching income by ID", e);
        }
        return null; // Return null if income not found or error occurs
    }

    public boolean createIncome(Income income) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO incomes(user_id, account_id, income_date, income_source, amount) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, income.getUserId());
                stmt.setInt(2, income.getAccountId());
                stmt.setDate(3, income.getIncomeDate());
                stmt.setString(4, income.getIncomeSource());
                stmt.setDouble(5, income.getAmount());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during income creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateIncome, deleteIncome)
}
