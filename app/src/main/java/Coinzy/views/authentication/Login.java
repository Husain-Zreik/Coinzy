package Coinzy.views.authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import Coinzy.database.DatabaseManager;
import Coinzy.models.UserSession;
import Coinzy.views.user.home.HomePage;

public class Login extends javax.swing.JFrame {
        private static final Logger logger = Logger.getLogger(Login.class.getName());

        public Login() {
                initComponents();
        }

        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                Right = new javax.swing.JPanel();
                jLabel5 = new javax.swing.JLabel();
                jLabel6 = new javax.swing.JLabel();
                Left = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                username = new javax.swing.JTextField();
                jLabel3 = new javax.swing.JLabel();
                password = new javax.swing.JPasswordField();
                jButton1 = new javax.swing.JButton();
                jLabel4 = new javax.swing.JLabel();
                jButton2 = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jPanel1.setBackground(new java.awt.Color(255, 255, 255));
                jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
                jPanel1.setLayout(null);

                Right.setBackground(new java.awt.Color(0, 102, 102));
                Right.setPreferredSize(new java.awt.Dimension(400, 500));

                jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logo.png"))); // NOI18N

                jLabel6.setFont(new java.awt.Font("Segoe Script", 1, 24)); // NOI18N
                jLabel6.setForeground(new java.awt.Color(255, 255, 255));
                jLabel6.setText("Finance Mangement");

                javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
                Right.setLayout(RightLayout);
                RightLayout.setHorizontalGroup(
                                RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(RightLayout.createSequentialGroup()
                                                                .addGroup(RightLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(RightLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(86, 86, 86)
                                                                                                .addComponent(jLabel5))
                                                                                .addGroup(RightLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(15, 15, 15)
                                                                                                .addComponent(jLabel6,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                270,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                RightLayout.setVerticalGroup(
                                RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(RightLayout.createSequentialGroup()
                                                                .addGap(108, 108, 108)
                                                                .addComponent(jLabel5)
                                                                .addGap(26, 26, 26)
                                                                .addComponent(jLabel6)
                                                                .addContainerGap(208, Short.MAX_VALUE)));

                jPanel1.add(Right);
                Right.setBounds(0, 0, 290, 500);

                Left.setBackground(new java.awt.Color(255, 255, 255));
                Left.setMinimumSize(new java.awt.Dimension(400, 500));

                jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 36)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(0, 102, 102));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("Login");

                jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
                jLabel2.setText("Username");

                username.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
                username.setForeground(new java.awt.Color(102, 102, 102));

                jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
                jLabel3.setText("Password");

                password.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                passwordActionPerformed(evt);
                        }
                });

                jButton1.setBackground(new java.awt.Color(0, 102, 102));
                jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                jButton1.setText("Login");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });

                jLabel4.setText("I don't have account");

                jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                jButton2.setForeground(new java.awt.Color(255, 51, 51));
                jButton2.setText("Sign Up");
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton2ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
                Left.setLayout(LeftLayout);
                LeftLayout.setHorizontalGroup(
                                LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(LeftLayout.createSequentialGroup()
                                                                .addGap(190, 190, 190)
                                                                .addComponent(jLabel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                106,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(LeftLayout.createSequentialGroup()
                                                                .addGroup(LeftLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(LeftLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(83, 83, 83)
                                                                                                .addGroup(LeftLayout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(password,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                340,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(jLabel3,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                67,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(jLabel2,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                83,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(username,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                340,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(LeftLayout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel4,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                119,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(jButton2))))
                                                                                .addGroup(LeftLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(200, 200, 200)
                                                                                                .addComponent(jButton1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                93,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(87, Short.MAX_VALUE)));
                LeftLayout.setVerticalGroup(
                                LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(LeftLayout.createSequentialGroup()
                                                                .addGap(78, 78, 78)
                                                                .addComponent(jLabel1)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(username,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(password,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                30,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(29, 29, 29)
                                                                .addComponent(jButton1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                33,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                106,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(LeftLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(jButton2))
                                                                .addGap(29, 29, 29)));

                jPanel1.add(Left);
                Left.setBounds(290, 0, 510, 500);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void passwordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_passwordActionPerformed
        }// GEN-LAST:event_passwordActionPerformed

        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
                SignUp signUpFrame = new SignUp();
                signUpFrame.setVisible(true);
                signUpFrame.pack();
                signUpFrame.setLocationRelativeTo(null);
                this.dispose();
        }

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
                var user_id = username.getText();
                var pass = String.valueOf(password.getPassword());

                if (user_id == null || user_id.isEmpty() || pass == null || pass.isEmpty()) {
                        JOptionPane.showMessageDialog(rootPane, "Please fill in both username and password fields.");
                        return;
                }

                if (attemptLogin(user_id, pass)) {
                        UserSession.userId = getUserId(user_id);
                        HomePage home = new HomePage();
                        home.setVisible(true);
                        home.pack();
                        home.setLocationRelativeTo(null);
                        this.dispose();
                } else {
                        JOptionPane.showMessageDialog(rootPane, "Invalid username or password.");
                }
        }

        private boolean attemptLogin(String user_id, String pass) {
                try (Connection conn = DatabaseManager.getConnection()) {
                        String sql = "SELECT * FROM users WHERE username=? AND password=?";
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                                stmt.setString(1, user_id);
                                stmt.setString(2, pass);
                                try (ResultSet rs = stmt.executeQuery()) {
                                        return rs.next();
                                }
                        }
                } catch (SQLException e) {
                        logger.log(Level.SEVERE, "SQL error occurred :", e);
                        return false;
                }
        }

        private int getUserId(String username) {
                try (Connection conn = DatabaseManager.getConnection()) {
                        String sql = "SELECT id FROM users WHERE username=?";
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                                stmt.setString(1, username);
                                try (ResultSet rs = stmt.executeQuery()) {
                                        if (rs.next()) {
                                                return rs.getInt("id");
                                        }
                                }
                        }
                } catch (SQLException e) {
                        logger.log(Level.SEVERE, "SQL error occurred :", e);
                }
                return -1;
        }

        public static void main(String args[]) {
                java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JPanel Left;
        private javax.swing.JPanel Right;
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPasswordField password;
        private javax.swing.JTextField username;
        // End of variables declaration//GEN-END:variables
}
