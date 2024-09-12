package Coinzy.views.admin.sections;

import java.awt.Color;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Coinzy.controllers.admin.ManagementController;
import Coinzy.models.User;
import Coinzy.views.admin.modals.AddUserView;

public class ManagementPanel extends javax.swing.JPanel {
    private JTable managementTable;
    private JButton addButton, editButton, deleteButton, viewButton, refreshButton, searchButton;
    private JTextField searchField;
    private JLabel statusLabel, searchExplanationLabel;
    private final ManagementController managementController;
    private List<User> users;

    public ManagementPanel() {
        this.managementController = new ManagementController();
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        // Explanation label above the search bar
        searchExplanationLabel = new JLabel("Search by ID, Username, Email, Status, or Role");

        // Search field with modern styling
        searchField = new JTextField(15);
        searchField.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchField.setFont(searchField.getFont().deriveFont(16f));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.DARK_GRAY);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBackground(Color.DARK_GRAY);
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(evt -> searchUsers());

        // Refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(Color.DARK_GRAY);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(evt -> loadUsers());

        // User management buttons
        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        deleteButton = new JButton("Delete User");
        viewButton = new JButton("View Details");
        addButton.addActionListener(evt -> addUser());
        editButton.addActionListener(evt -> editUser());
        deleteButton.addActionListener(evt -> deleteUser());
        viewButton.addActionListener(evt -> viewSelectedUserDetails());

        // Table to display user data
        managementTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(managementTable);

        // Status label
        statusLabel = new JLabel();

        // Layout for the panel
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(searchExplanationLabel)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(searchField)
                                                .addGap(18, 18, 18)
                                                .addComponent(searchButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(refreshButton))
                                        .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 800,
                                                Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(addButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(editButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(deleteButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(viewButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(searchExplanationLabel)
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(searchField)
                                        .addComponent(searchButton)
                                        .addComponent(refreshButton))
                                .addGap(18, 18, 18)
                                .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 275,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addButton)
                                        .addComponent(editButton)
                                        .addComponent(deleteButton)
                                        .addComponent(viewButton)
                                        .addComponent(statusLabel))
                                .addContainerGap(20, Short.MAX_VALUE)));

        customizeTable();
    }

    // Load users into the table
    private void loadUsers() {
        users = managementController.getUsers();
        if (users.isEmpty()) {
            showEmptyState();
        } else {
            populateTable(users);
        }
    }

    // Populate the table with users
    private void populateTable(List<User> users) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "ID", "Name", "Email", "Username", "Role", "Status" }, 0);

        for (User user : users) {
            model.addRow(new Object[] {
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getRoleName(),
                    user.getStatus()
            });
        }

        managementTable.setModel(model);
        customizeTable();
    }

    // Customize the table styling
    private void customizeTable() {
        managementTable.setRowHeight(30);
        managementTable.getTableHeader().setReorderingAllowed(false);
        managementTable.getTableHeader().setFont(managementTable.getTableHeader().getFont().deriveFont(16f));
        managementTable.setFont(managementTable.getFont().deriveFont(14f));

        // Set the cell renderer to center-align text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < managementTable.getColumnCount(); i++) {
            managementTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // Display an empty state when no users are found
    private void showEmptyState() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "ID", "Name", "Email", "Username", "Role", "Status" }, 0);
        model.addRow(new Object[] { "No Data", "No Data", "No Data", "No Data", "No Data", "No Data" });
        managementTable.setModel(model);
        customizeTable();
    }

    // Search users based on input
    private void searchUsers() {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            statusLabel.setText("Enter a keyword to search.");
            loadUsers(); // Load all users if the search is empty
        } else {
            List<User> searchResults = managementController.searchUsers(keyword);
            if (searchResults.isEmpty()) {
                statusLabel.setText("No users found.");
                showEmptyState(); // Show empty state if no users are found
            } else {
                populateTable(searchResults);
            }
        }
    }

    // View details of the selected user
    private void viewSelectedUserDetails() {
        int selectedRow = managementTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = users.get(selectedRow);
            showUserDetails(user);
        } else {
            statusLabel.setText("Please select a user to view details.");
        }
    }

    // Display user details in a modal
    private void showUserDetails(User user) {
        JOptionPane.showMessageDialog(this,
                "User Details:\n\nID: " + user.getId() + "\nName: " + user.getName() + "\nEmail: " + user.getEmail()
                        + "\nUsername: " + user.getUsername() + "\nRole: " + user.getRoleName() + "\nStatus: "
                        + user.getStatus(),
                "User Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // Action to handle adding a user
    private void addUser() {
        // Show modal dialog to add a new user
        AddUserView userDialog = new AddUserView(); // Pass 'this' as the parent frame
        userDialog.setVisible(true);
        // Reload users if the dialog was closed and user was added
        loadUsers();
    }

    // Action to handle editing a user
    private void editUser() {
        // Edit selected user
        // ...
    }

    // Action to handle deleting a user
    private void deleteUser() {
        int selectedRow = managementTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = users.get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete user: " + user.getUsername() + "? Type 'delete' to confirm.",
                    "Delete User", JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                managementController.deleteUser(user);
                loadUsers(); // Refresh the user list
            }
        }
    }
}
