package Coinzy.controllers.admin;

import java.util.List;

import Coinzy.models.User;
import Coinzy.providers.UserProvider;

public class RequestsController {
    private final UserProvider userProvider;

    public RequestsController() {
        this.userProvider = new UserProvider();
    }

    // Fetch all pending registration requests
    public List<User> getPendingRequests() {
        return userProvider.getUsersByStatus("pending");
    }

    // Accept the registration request
    public boolean acceptRequest(User user) {
        user.setStatus("approved");
        return userProvider.updateStatus(user.getId(), "approved");
    }

    // Decline the registration request
    public boolean declineRequest(User user) {
        user.setStatus("Rejected");
        return userProvider.updateStatus(user.getId(), "rejected");
    }
}
