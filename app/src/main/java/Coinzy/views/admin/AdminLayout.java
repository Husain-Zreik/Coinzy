/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Coinzy.views.admin;

import java.awt.CardLayout;

import javax.swing.JPanel;

import Coinzy.views.admin.sections.AnnouncementPanel;
import Coinzy.views.admin.sections.ManagementPanel;
import Coinzy.views.admin.sections.OverviewPanel;
import Coinzy.views.admin.sections.RequestsPanel;
import Coinzy.views.authentication.LoginView;

/**
 *
 * @author Administrator
 */
public class AdminLayout extends javax.swing.JFrame {

    private OverviewPanel overviewPanel;
    private ManagementPanel managementPanel;
    private RequestsPanel requestsPanel;
    private AnnouncementPanel announcementsPanel;

    /**
     * Creates new form AdminHomeView
     */
    public AdminLayout() {
        initComponents();
        overviewPanel = new OverviewPanel();
        managementPanel = new ManagementPanel();
        requestsPanel = new RequestsPanel();
        announcementsPanel = new AnnouncementPanel();

        // Add the panel instances to the ContentPanel (CardLayout)
        ContentPanel.setLayout(new CardLayout());
        ContentPanel.add(overviewPanel, "Overview");
        ContentPanel.add(managementPanel, "Management");
        ContentPanel.add(requestsPanel, "Requests");
        ContentPanel.add(announcementsPanel, "Announcements");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Navigation = new javax.swing.ButtonGroup();
        Layout = new javax.swing.JPanel();
        NavigationPanel = new javax.swing.JPanel();
        Overview = new javax.swing.JToggleButton();
        Management = new javax.swing.JToggleButton();
        Requests = new javax.swing.JToggleButton();
        Announcements = new javax.swing.JToggleButton();
        Logout = new javax.swing.JToggleButton();
        ContentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));

        Layout.setLayout(new java.awt.BorderLayout());

        NavigationPanel.setBackground(new java.awt.Color(0, 102, 102));
        NavigationPanel.setToolTipText("Menu");
        NavigationPanel.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        NavigationPanel.setPreferredSize(new java.awt.Dimension(240, 80));
        NavigationPanel.setLayout(new java.awt.GridLayout(1, 5));

        Overview.setBackground(new java.awt.Color(0, 102, 102));
        Navigation.add(Overview);
        Overview.setFont(new java.awt.Font("Dubai", 1, 16)); // NOI18N
        Overview.setForeground(new java.awt.Color(255, 255, 255));
        Overview.setText("Overview");
        Overview.setBorder(null);
        Overview.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Overview.setName("Overview"); // NOI18N
        Overview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OverviewActionPerformed(evt);
            }
        });
        NavigationPanel.add(Overview);

        Management.setBackground(new java.awt.Color(0, 102, 102));
        Navigation.add(Management);
        Management.setFont(new java.awt.Font("Dubai", 1, 16)); // NOI18N
        Management.setForeground(new java.awt.Color(255, 255, 255));
        Management.setText("Management");
        Management.setBorder(null);
        Management.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Management.setMaximumSize(new java.awt.Dimension(33, 16));
        Management.setMinimumSize(new java.awt.Dimension(33, 16));
        Management.setPreferredSize(new java.awt.Dimension(33, 16));
        Management.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManagementActionPerformed(evt);
            }
        });
        NavigationPanel.add(Management);

        Requests.setBackground(new java.awt.Color(0, 102, 102));
        Navigation.add(Requests);
        Requests.setFont(new java.awt.Font("Dubai", 1, 16)); // NOI18N
        Requests.setForeground(new java.awt.Color(255, 255, 255));
        Requests.setText("Requests");
        Requests.setBorder(null);
        Requests.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Requests.setMaximumSize(new java.awt.Dimension(33, 16));
        Requests.setMinimumSize(new java.awt.Dimension(33, 16));
        Requests.setPreferredSize(new java.awt.Dimension(33, 16));
        Requests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RequestsActionPerformed(evt);
            }
        });
        NavigationPanel.add(Requests);

        Announcements.setBackground(new java.awt.Color(0, 102, 102));
        Navigation.add(Announcements);
        Announcements.setFont(new java.awt.Font("Dubai", 1, 16)); // NOI18N
        Announcements.setForeground(new java.awt.Color(255, 255, 255));
        Announcements.setText("Announcements");
        Announcements.setBorder(null);
        Announcements.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Announcements.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnnouncementsActionPerformed(evt);
            }
        });
        NavigationPanel.add(Announcements);

        Logout.setBackground(new java.awt.Color(0, 102, 102));
        Navigation.add(Logout);
        Logout.setFont(new java.awt.Font("Dubai", 1, 16)); // NOI18N
        Logout.setForeground(new java.awt.Color(255, 255, 255));
        Logout.setText("Log Out");
        Logout.setBorder(null);
        Logout.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        NavigationPanel.add(Logout);

        Layout.add(NavigationPanel, java.awt.BorderLayout.PAGE_START);

        ContentPanel.setBackground(new java.awt.Color(204, 204, 204));
        ContentPanel.setLayout(new java.awt.CardLayout());
        Layout.add(ContentPanel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Layout, javax.swing.GroupLayout.PREFERRED_SIZE, 772,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Layout, javax.swing.GroupLayout.PREFERRED_SIZE, 520,
                                javax.swing.GroupLayout.PREFERRED_SIZE));

        setBounds(0, 0, 786, 527);
    }// </editor-fold>//GEN-END:initComponents

    private void OverviewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_OverviewActionPerformed
        // Switch to the Overview panel
        CardLayout cardLayout = (CardLayout) ContentPanel.getLayout();
        cardLayout.show(ContentPanel, "Overview");
    }// GEN-LAST:event_OverviewActionPerformed

    private void ManagementActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ManagementActionPerformed
        // Switch to the Management panel
        CardLayout cardLayout = (CardLayout) ContentPanel.getLayout();
        cardLayout.show(ContentPanel, "Management");
    }// GEN-LAST:event_ManagementActionPerformed

    private void RequestsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_RequestsActionPerformed
        // Switch to the Requests panel
        CardLayout cardLayout = (CardLayout) ContentPanel.getLayout();
        cardLayout.show(ContentPanel, "Requests");
    }// GEN-LAST:event_RequestsActionPerformed

    private void AnnouncementsActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_AnnouncementsActionPerformed
        // Switch to the Announcements panel
        CardLayout cardLayout = (CardLayout) ContentPanel.getLayout();
        cardLayout.show(ContentPanel, "Announcements");
    }// GEN-LAST:event_AnnouncementsActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_LogoutActionPerformed
        // Close the admin dashboard
        this.dispose();

        // Open the login window
        LoginView loginView = new LoginView(); // Assuming you have a class called LoginView for the login screen
        loginView.setLocationRelativeTo(null); // Center the login window
        loginView.setVisible(true);
    }// GEN-LAST:event_LogoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminLayout.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminLayout.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminLayout.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminLayout.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AdminLayout adminLayout = new AdminLayout();
                adminLayout.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Set to fullscreen
                adminLayout.setVisible(true); // Display the JFrame
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton Announcements;
    private javax.swing.JPanel ContentPanel;
    private javax.swing.JPanel Layout;
    private javax.swing.JToggleButton Logout;
    private javax.swing.JToggleButton Management;
    private javax.swing.ButtonGroup Navigation;
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JToggleButton Overview;
    private javax.swing.JToggleButton Requests;
    // End of variables declaration//GEN-END:variables
}
