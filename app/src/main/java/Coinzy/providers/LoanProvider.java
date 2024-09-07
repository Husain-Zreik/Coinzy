package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoanProvider {
    private static final Logger logger = Logger.getLogger(LoanProvider.class.getName());

    // Create a new loan
    public boolean createLoan(Loan loan) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO loans (user_id, amount, repayment_date, repayment_frequency, is_given, recipient_name, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, loan.getUserId());
                pstmt.setBigDecimal(2, loan.getAmount());
                pstmt.setDate(3, loan.getRepaymentDate());
                pstmt.setString(4, loan.getRepaymentFrequency());
                pstmt.setBoolean(5, loan.isGiven());
                pstmt.setString(6, loan.getRecipientName());
                pstmt.setString(7, loan.getNote());

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during loan creation", e);
        }
        return false;
    }

    // Get a loan by ID
    public Loan getLoanById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM loans WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToLoan(rs);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching loan by ID", e);
        }
        return null;
    }

    // Update a loan
    public boolean updateLoan(Loan loan) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE loans SET user_id = ?, amount = ?, repayment_date = ?, repayment_frequency = ?, is_given = ?, recipient_name = ?, note = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, loan.getUserId());
                pstmt.setBigDecimal(2, loan.getAmount());
                pstmt.setDate(3, loan.getRepaymentDate());
                pstmt.setString(4, loan.getRepaymentFrequency());
                pstmt.setBoolean(5, loan.isGiven());
                pstmt.setString(6, loan.getRecipientName());
                pstmt.setString(7, loan.getNote());
                pstmt.setInt(8, loan.getId());

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during loan update", e);
        }
        return false;
    }

    // Delete a loan
    public boolean deleteLoan(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM loans WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsDeleted = pstmt.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during loan deletion", e);
        }
        return false;
    }

    // Get all loans for a user
    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM loans WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        loans.add(mapRowToLoan(rs));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching loans by user ID", e);
        }
        return loans;
    }

    // Map ResultSet to Loan
    private Loan mapRowToLoan(ResultSet rs) throws SQLException {
        return new Loan(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getBigDecimal("amount"),
                rs.getDate("repayment_date"),
                rs.getString("repayment_frequency"),
                rs.getBoolean("is_given"),
                rs.getString("recipient_name"),
                rs.getString("note"));
    }
}
