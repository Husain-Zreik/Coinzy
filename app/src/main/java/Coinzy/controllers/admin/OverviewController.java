package Coinzy.controllers.admin;

import java.util.Map;
import Coinzy.providers.TransactionProvider;
import Coinzy.providers.UserProvider;

public class OverviewController {
    private final UserProvider userProvider;
    private final TransactionProvider transactionProvider;

    public OverviewController() {
        this.userProvider = new UserProvider();
        this.transactionProvider = new TransactionProvider();
    }

    // Get total number of users
    public int getTotalUsers() {
        return userProvider.getTotalUsersCount();
    }

    public int getRejectedUsersCount() {
        return userProvider.getUsersByStatusCount("rejected");
    }

    // Get count of blocked users
    public int getBlockedUsersCount() {
        return userProvider.getUsersByStatusCount("blocked");
    }

    // Get number of pending registration requests
    public int getPendingRequests() {
        return userProvider.getUsersByStatusCount("pending");
    }

    // Get total number of transactions
    public int getTotalTransactions() {
        return transactionProvider.getTotalTransactionCount();
    }

    // Get total revenue from all completed transactions
    public double getTotalRevenue() {
        return transactionProvider.getTotalRevenue();
    }

    // Get total expenses from all completed transactions
    public double getTotalExpenses() {
        return transactionProvider.getTotalExpenses();
    }

    // Get user growth by month
    public Map<String, Integer> getUserGrowthByMonth() {
        return userProvider.getUserGrowthByMonth();
    }
}
