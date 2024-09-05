package Coinzy;

import javax.swing.SwingUtilities;

import Coinzy.views.authentication.Login;

public class App {

    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure the GUI creation runs on the Event
        // Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create and show the Login frame
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null); // Center the frame on the screen
        });
    }
}
