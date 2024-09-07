package Coinzy.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int id;
    private int userId;
    private int categoryId;
    private BigDecimal amount;
    private LocalDate expenseDate; // Changed to LocalDate
    private String recurrencePattern;
    private String note;

    // Default constructor
    public Expense() {
    }

    // Parameterized constructor
    public Expense(int id, int userId, int categoryId, BigDecimal amount, LocalDate expenseDate,
            String recurrencePattern,
            String note) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.expenseDate = expenseDate;
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

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
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
        return "Expense{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                ", recurrencePattern='" + recurrencePattern + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
