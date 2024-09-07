package Coinzy.models;

import java.math.BigDecimal;
import java.sql.Date;

public class Loan {
    private int id;
    private int userId;
    private BigDecimal amount;
    private Date repaymentDate;
    private String repaymentFrequency;
    private boolean isGiven;
    private String recipientName;
    private String note;

    // Default constructor
    public Loan() {
    }

    // Parameterized constructor
    public Loan(int id, int userId, BigDecimal amount, Date repaymentDate, String repaymentFrequency, boolean isGiven,
            String recipientName, String note) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.repaymentDate = repaymentDate;
        this.repaymentFrequency = repaymentFrequency;
        this.isGiven = isGiven;
        this.recipientName = recipientName;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getRepaymentFrequency() {
        return repaymentFrequency;
    }

    public void setRepaymentFrequency(String repaymentFrequency) {
        this.repaymentFrequency = repaymentFrequency;
    }

    public boolean isGiven() {
        return isGiven;
    }

    public void setGiven(boolean given) {
        isGiven = given;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", repaymentDate=" + repaymentDate +
                ", repaymentFrequency='" + repaymentFrequency + '\'' +
                ", isGiven=" + isGiven +
                ", recipientName='" + recipientName + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
