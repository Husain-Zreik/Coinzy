package Coinzy;

import javax.swing.SwingUtilities;

import Coinzy.views.authentication.LoginView;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            loginView.pack();
            loginView.setLocationRelativeTo(null);
        });
    }
}
