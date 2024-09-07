package Coinzy.controllers.authentication;

import Coinzy.providers.UserProvider;
import Coinzy.views.authentication.LoginView;
import Coinzy.views.authentication.SignupView;
import Coinzy.models.User;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignupController {
    private static final Logger logger = Logger.getLogger(SignupController.class.getName());
    private final SignupView view;
    private final UserProvider userProvider;
    private LoginView loginView; // Added LoginView instance

    public SignupController(SignupView view) {
        this.view = view;
        this.userProvider = new UserProvider();
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void signup(String name, String username, String email, String password, int roleId) {
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Please fill in the following field(s):");
            if (name.isEmpty())
                errorMessage.append("\n- Name");
            if (username.isEmpty())
                errorMessage.append("\n- Username");
            if (email.isEmpty())
                errorMessage.append("\n- Email");
            if (password.isEmpty())
                errorMessage.append("\n- Password");

            view.displayError(errorMessage.toString());
            return;
        }

        try {
            validatePassword(password);

            if (userProvider.isUserExists(username)) {
                view.displayError("Username already exists. Please choose another one.");
                return;
            }

            // Create User object
            User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email); // Set email
            user.setPassword(password);
            user.setRoleId(roleId); // Set role ID based on radio button selection
            user.setStatus("pending");
            user.setOwnerId(null); // or set to appropriate value if applicable

            boolean success = userProvider.createUser(user);
            if (success) {
                view.clearFields();
                JOptionPane.showMessageDialog(view, "Account successfully created!");

                if (loginView != null) {
                    loginView.setVisible(true); // Show the login view
                    loginView.pack();
                    loginView.setLocationRelativeTo(null);
                }
                view.dispose(); // Close the signup view
            } else {
                view.displayError("Error creating account.");
            }

        } catch (Exception e) {
            view.displayError("Error: " + e.getMessage());
            logger.log(Level.SEVERE, "Signup error occurred", e);
        }
    }

    private void validatePassword(String password) throws Exception {
        int minLength = 8;
        if (password.length() < minLength) {
            throw new Exception("Password should be at least " + minLength + " characters long.");
        }

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            throw new Exception("Password should contain at least one special character.");
        }
    }
}
