package Coinzy.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Transaction;

public class TransactionProvider {
    private static final Logger logger = Logger.getLogger(TransactionProvider.class.getName());

    // Create a new transaction
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (wallet_id, amount, transaction_type, description, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getWalletId());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setString(3, transaction.getType());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setTimestamp(5, transaction.getTimestamp());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during transaction creation", e);
        }
        return false;
    }

    // Get a transaction by ID
    public Transaction getTransactionById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToTransaction(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching transaction by ID", e);
        }
        return null;
    }

    // Update a transaction
    public boolean updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET wallet_id = ?, amount = ?, transaction_type = ?, description = ?, timestamp = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getWalletId());
            pstmt.setBigDecimal(2, transaction.getAmount());
            pstmt.setString(3, transaction.getType());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setTimestamp(5, transaction.getTimestamp());
            pstmt.setInt(6, transaction.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during transaction update", e);
        }
        return false;
    }

    // Delete a transaction
    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during transaction deletion", e);
        }
        return false;
    }

    // Get all transactions for a wallet
    public List<Transaction> getTransactionsByWalletId(int walletId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE wallet_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, walletId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching transactions by wallet ID", e);
        }
        return transactions;
    }

    // Get total number of transactions
    public int getTotalTransactionCount() {
        String sql = "SELECT COUNT(*) FROM transactions";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching total transaction count", e);
        }
        return 0;
    }

    // Get number of transactions by status (assuming status column exists)
    public int getTransactionsByStatusCount(String status) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE status = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching transaction count by status", e);
        }
        return 0;
    }

    // Get total revenue from completed transactions
    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) FROM transactions WHERE transaction_type = 'credit'"; // Adjust as needed
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching total revenue", e);
        }
        return 0.0;
    }

    // Get total expenses from transactions (assuming 'debit' indicates expenses)
    public double getTotalExpenses() {
        String sql = "SELECT SUM(amount) FROM transactions WHERE transaction_type = 'debit'"; // Adjust as needed
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching total expenses", e);
        }
        return 0.0;
    }

    // Map ResultSet to Transaction
    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("wallet_id"),
                rs.getString("transaction_type"),
                rs.getBigDecimal("amount"),
                rs.getString("description"),
                rs.getTimestamp("timestamp"));
    }
}
