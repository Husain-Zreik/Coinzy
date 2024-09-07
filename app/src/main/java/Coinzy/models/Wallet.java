package Coinzy.models;

import java.math.BigDecimal;

public class Wallet {
    private int id;
    private int userId;
    private String walletName;
    private BigDecimal balance;

    // Default constructor
    public Wallet() {
    }

    // Parameterized constructor
    public Wallet(int id, int userId, String walletName, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.walletName = walletName;
        this.balance = balance;
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

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", userId=" + userId +
                ", walletName='" + walletName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
