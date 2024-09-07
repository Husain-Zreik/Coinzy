package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountProvider {
    private static final Logger logger = Logger.getLogger(AccountProvider.class.getName());

    public Account getAccountById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM accounts WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Account account = new Account();
                        account.setId(rs.getInt("id"));
                        account.setUserId(rs.getInt("user_id"));
                        account.setAccountType(rs.getString("account_type"));
                        account.setBalance(rs.getDouble("balance"));
                        account.setLiabilities(rs.getDouble("liabilities"));
                        return account;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching account by ID", e);
        }
        return null; // Return null if account not found or error occurs
    }

    public boolean createAccount(Account account) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO accounts(user_id, account_type, balance, liabilities) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, account.getUserId());
                stmt.setString(2, account.getAccountType());
                stmt.setDouble(3, account.getBalance());
                stmt.setDouble(4, account.getLiabilities());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during account creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateAccount, deleteAccount)
}
