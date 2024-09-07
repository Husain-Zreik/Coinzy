package Coinzy.providers;

import Coinzy.database.DatabaseManager;
import Coinzy.models.FinancialCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinancialCategoryProvider {
    private static final Logger logger = Logger.getLogger(FinancialCategoryProvider.class.getName());

    // Create a new financial category
    public boolean createCategory(FinancialCategory category) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO financial_categories (category_name, category_type) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, category.getCategoryName());
                pstmt.setString(2, category.getCategoryType());

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during category creation", e);
        }
        return false;
    }

    // Get a financial category by ID
    public FinancialCategory getCategoryById(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM financial_categories WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToCategory(rs);
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching category by ID", e);
        }
        return null;
    }

    // Update a financial category
    public boolean updateCategory(FinancialCategory category) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE financial_categories SET category_name = ?, category_type = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, category.getCategoryName());
                pstmt.setString(2, category.getCategoryType());
                pstmt.setInt(3, category.getId());

                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during category update", e);
        }
        return false;
    }

    // Delete a financial category
    public boolean deleteCategory(int id) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "DELETE FROM financial_categories WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsDeleted = pstmt.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred during category deletion", e);
        }
        return false;
    }

    // Get all financial categories
    public List<FinancialCategory> getAllCategories() {
        List<FinancialCategory> categories = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM financial_categories";
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    categories.add(mapRowToCategory(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL error occurred when fetching all categories", e);
        }
        return categories;
    }

    // Map ResultSet to FinancialCategory
    private FinancialCategory mapRowToCategory(ResultSet rs) throws SQLException {
        return new FinancialCategory(
                rs.getInt("id"),
                rs.getString("category_name"),
                rs.getString("category_type"));
    }
}
