package Coinzy.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int walletId;
    private String type;
    private BigDecimal amount;
    private String description;
    private Timestamp timestamp;

    // Default constructor
    public Transaction() {
    }

    // Parameterized constructor
    public Transaction(int id, int walletId, String type, BigDecimal amount, String description, Timestamp timestamp) {
        this.id = id;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", walletId=" + walletId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
