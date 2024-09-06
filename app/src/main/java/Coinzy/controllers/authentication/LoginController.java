package Coinzy.controllers.authentication;

import Coinzy.providers.UserProvider;
import Coinzy.models.UserSession;
import Coinzy.views.user.home.HomePage;

import javax.swing.JOptionPane;

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

        if (userProvider.isValidUser(username, password)) {
            UserSession.userId = userProvider.getUserId(username);
            HomePage home = new HomePage();
            home.setVisible(true);
            home.pack();
            home.setLocationRelativeTo(null);
            loginView.dispose(); // Close the login view after successful login
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }
    }
}
