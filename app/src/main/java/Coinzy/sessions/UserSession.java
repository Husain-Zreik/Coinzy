package Coinzy.sessions;

import Coinzy.models.User;

public class UserSession {
    public static int userId;

    private User currentUser;

    // Method to set the current user after successful login
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Method to retrieve the current user
    public User getCurrentUser() {
        return currentUser;
    }

    // Method to clear the current user (e.g., on logout)
    public void clearCurrentUser() {
        currentUser = null;
    }

    // Method to check if a user is currently logged in
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
}
