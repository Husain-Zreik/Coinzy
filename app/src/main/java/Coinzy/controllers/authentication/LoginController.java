package Coinzy.controllers.authentication;

import javax.swing.JOptionPane;

import Coinzy.models.User;
import Coinzy.providers.UserProvider;
import Coinzy.sessions.UserSession;
import Coinzy.views.admin.AdminLayout;
import Coinzy.views.user.home.HomePage;

public class LoginController {
    private final UserProvider userProvider;

    public LoginController() {
        userProvider = new UserProvider();
    }

    public void handleLogin(String username, String password, javax.swing.JFrame loginView) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in both username and password fields.");
            return;
        }

        User user = userProvider.getUser(username);

        if (user == null || !userProvider.isValidUser(username, password)) {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
            return;
        }

        // Check user status
        String status = user.getStatus();
        if (!"approved".equalsIgnoreCase(status)) {
            if ("pending".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(null,
                        "Your account is pending approval. Please wait for admin approval.");
            } else if ("rejected".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(null, "Your account has been rejected.");
            } else if ("blocked".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(null, "Your account is blocked.");
            }
            return;
        }

        // Proceed with login if status is approved
        UserSession.userId = user.getId();
        String role = user.getRoleName();
        loginView.dispose();

        if ("admin".equalsIgnoreCase(role)) {
            AdminLayout adminLayout = new AdminLayout();
            adminLayout.setVisible(true);
            adminLayout.pack();
            adminLayout.setLocationRelativeTo(null);
        } else {
            HomePage home = new HomePage();
            home.setVisible(true);
            home.pack();
            home.setLocationRelativeTo(null);
        }
    }
}
