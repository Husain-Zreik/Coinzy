package Coinzy.controllers.admin;

import java.util.List;

import Coinzy.models.User;
import Coinzy.providers.UserProvider;

public class ManagementController {
    private final UserProvider userProvider;

    public ManagementController() {
        this.userProvider = new UserProvider();
    }

    // Fetch all users
    public List<User> getUsers() {
        return userProvider.getAllUsers();
    }

    // Add a new user (you can enhance this method based on your add user
    // functionality)
    public boolean addUser(User user) {
        return userProvider.createUser(user);
    }

    // Edit an existing user
    public boolean editUser(User user) {
        return userProvider.updateUser(user);
    }

    // Delete a user
    public boolean deleteUser(User user) {
        return userProvider.deleteUser(user.getId());
    }

    public List<User> searchUsers(String query) {
        return userProvider.searchUsers(query);
    }

    public boolean updateUserStatus(int userId, String newStatus) {
        return userProvider.updateStatus(userId, newStatus);
    }
}
