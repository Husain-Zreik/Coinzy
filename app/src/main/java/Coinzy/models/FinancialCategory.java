package Coinzy.models;

public class FinancialCategory {
    private int id;
    private String categoryName;
    private String categoryType;

    // Default constructor
    public FinancialCategory() {
    }

    // Parameterized constructor
    public FinancialCategory(int id, String categoryName, String categoryType) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public String toString() {
        return "FinancialCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType='" + categoryType + '\'' +
                '}';
    }
}
