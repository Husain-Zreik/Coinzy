package Coinzy.controllers.authentication;

import Coinzy.providers.UserProvider;
import Coinzy.views.authentication.LoginView;
import Coinzy.views.authentication.SignupView;

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

    public void signup(String name, String username, String password) {
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Please fill in the following field(s):");
            if (name.isEmpty())
                errorMessage.append("\n- Name");
            if (username.isEmpty())
                errorMessage.append("\n- Username");
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

            boolean success = userProvider.createUser(name, username, password);
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
