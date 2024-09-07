package Coinzy.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income {
    private int id;
    private int userId;
    private int categoryId;
    private BigDecimal amount;
    private LocalDate incomeDate; // Use LocalDate instead of java.sql.Date
    private String recurrencePattern;
    private String note;

    // Default constructor
    public Income() {
    }

    // Parameterized constructor
    public Income(int id, int userId, int categoryId, BigDecimal amount, LocalDate incomeDate, String recurrencePattern,
            String note) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.recurrencePattern = recurrencePattern;
        this.note = note;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", incomeDate=" + incomeDate +
                ", recurrencePattern='" + recurrencePattern + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
