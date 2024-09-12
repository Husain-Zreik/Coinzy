package Coinzy.controllers.admin;

import Coinzy.providers.UserProvider;
import Coinzy.models.User;
import Coinzy.views.admin.modals.AddUserView;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDialogController {
    private static final Logger logger = Logger.getLogger(UserDialogController.class.getName());
    private final AddUserView view;
    private final UserProvider userProvider;

    public UserDialogController(AddUserView addUserView) {
        this.view = addUserView;
        this.userProvider = new UserProvider();
    }

    public void addUser(String name, String username, String email, String password, int roleId) {
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
            user.setStatus("approved"); // Assuming status for new users is "active"
            user.setOwnerId(null); // or set to appropriate value if applicable

            boolean success = userProvider.createUser(user);
            if (success) {
                view.clearFields();
                JOptionPane.showMessageDialog(view, "User successfully added!");
                view.dispose(); // Close the dialog
            } else {
                view.displayError("Error adding user.");
            }

        } catch (Exception e) {
            view.displayError("Error: " + e.getMessage());
            logger.log(Level.SEVERE, "User addition error occurred", e);
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
