package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Wallet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WalletProvider {
    private static final Logger logger = Logger.getLogger(WalletProvider.class.getName());

    // Create a new wallet
    public boolean createWallet(Wallet wallet) {
        String sql = "INSERT INTO wallets (user_id, balance, wallet_name) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wallet.getUserId());
            pstmt.setBigDecimal(2, wallet.getBalance());
            pstmt.setString(3, wallet.getWalletName());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during wallet creation", e);
        }
        return false;
    }

    // Get a wallet by ID
    public Wallet getWalletById(int id) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToWallet(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching wallet by ID", e);
        }
        return null;
    }

    // Update a wallet
    public boolean updateWallet(Wallet wallet) {
        String sql = "UPDATE wallets SET user_id = ?, balance = ?, wallet_name = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, wallet.getUserId());
            pstmt.setBigDecimal(2, wallet.getBalance());
            pstmt.setString(3, wallet.getWalletName());
            pstmt.setInt(4, wallet.getId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during wallet update", e);
        }
        return false;
    }

    // Delete a wallet
    public boolean deleteWallet(int id) {
        String sql = "DELETE FROM wallets WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during wallet deletion", e);
        }
        return false;
    }

    // Get all wallets for a user
    public List<Wallet> getWalletsByUserId(int userId) {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT * FROM wallets WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    wallets.add(mapRowToWallet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching wallets by user ID", e);
        }
        return wallets;
    }

    // Map ResultSet to Wallet
    private Wallet mapRowToWallet(ResultSet rs) throws SQLException {
        return new Wallet(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("wallet_name"),
                rs.getBigDecimal("balance"));
    }
}
