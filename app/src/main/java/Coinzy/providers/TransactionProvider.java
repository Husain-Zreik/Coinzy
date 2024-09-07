package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionProvider {
    private static final Logger logger = Logger.getLogger(TransactionProvider.class.getName());

    public Transaction getTransactionById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Transaction transaction = new Transaction();
                        transaction.setId(rs.getInt("id"));
                        transaction.setAccountId(rs.getInt("account_id"));
                        transaction.setType(rs.getString("type"));
                        transaction.setAmount(rs.getDouble("amount"));
                        transaction.setStatement(rs.getString("statement"));
                        transaction.setTime(rs.getTimestamp("time"));
                        return transaction;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching transaction by ID", e);
        }
        return null; // Return null if transaction not found or error occurs
    }

    public boolean createTransaction(Transaction transaction) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO transactions(account_id, type, amount, statement, time) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, transaction.getAccountId());
                stmt.setString(2, transaction.getType());
                stmt.setDouble(3, transaction.getAmount());
                stmt.setString(4, transaction.getStatement());
                stmt.setTimestamp(5, transaction.getTime());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during transaction creation", e);
        }
        return false;
    }

    // Add more methods as needed (e.g., updateTransaction, deleteTransaction)
}
