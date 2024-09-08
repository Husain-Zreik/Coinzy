package Coinzy.views.admin.sections;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Coinzy.controllers.admin.RequestsController;
import Coinzy.models.User;

public class RequestsPanel extends JPanel {
    private JTable requestsTable;
    private JButton acceptButton;
    private JButton declineButton;
    private JLabel statusLabel;
    private final RequestsController requestsController;
    private List<User> pendingRequests;

    public RequestsPanel() {
        this.requestsController = new RequestsController();
        initComponents();
        loadPendingRequests();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin for components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Table to display user registration requests
        requestsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);

        // Buttons for accepting or declining the selected request
        acceptButton = new JButton("Accept");
        declineButton = new JButton("Decline");

        // Button panel for alignment and spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);

        // Status label for feedback
        statusLabel = new JLabel("Select a request to approve or decline.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLUE);

        // Add action listeners for buttons
        acceptButton.addActionListener(evt -> acceptRequest());
        declineButton.addActionListener(evt -> declineRequest());

        // Add components to the panel with improved layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        add(tableScrollPane, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        add(buttonPanel, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusLabel, gbc);

        customizeTable();
    }

    // Load pending requests into the table
    private void loadPendingRequests() {
        pendingRequests = requestsController.getPendingRequests();
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Username", "Email", "Role", "Status" }, 0);

        for (User user : pendingRequests) {
            model.addRow(new Object[] {
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoleName(),
                    user.getStatus()
            });
        }

        requestsTable.setModel(model);
        customizeTable(); // Call after setting the model
    }

    private void customizeTable() {
        requestsTable.setRowHeight(35);
        requestsTable.getTableHeader().setReorderingAllowed(false);

        // Header font and background
        JTableHeader header = requestsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(63, 81, 181)); // Material blue
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder());

        // Set the table's font
        requestsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        requestsTable.setShowVerticalLines(false);
        requestsTable.setIntercellSpacing(new Dimension(0, 0));

        // Alternate row colors for better readability
        requestsTable.setDefaultRenderer(Object.class, new ModernTableCellRenderer());

        // Hover effect for the rows
        requestsTable.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = requestsTable.rowAtPoint(e.getPoint());
                requestsTable.setRowSelectionInterval(row, row);
            }
        });
    }

    // Action to handle accepting a request
    private void acceptRequest() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow != -1) {
            User selectedUser = pendingRequests.get(selectedRow);
            boolean success = requestsController.acceptRequest(selectedUser);
            if (success) {
                statusLabel.setText("Request approved.");
                statusLabel.setForeground(Color.GREEN);
                loadPendingRequests(); // Reload the table data after accepting the request
            } else {
                statusLabel.setText("Error accepting request.");
                statusLabel.setForeground(Color.RED);
            }
        } else {
            statusLabel.setText("No request selected.");
            statusLabel.setForeground(Color.RED);
        }
    }

    // Action to handle declining a request
    private void declineRequest() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow != -1) {
            User selectedUser = pendingRequests.get(selectedRow);
            boolean success = requestsController.declineRequest(selectedUser);
            if (success) {
                statusLabel.setText("Request rejected.");
                statusLabel.setForeground(Color.ORANGE);
                loadPendingRequests(); // Reload the table data after declining the request
            } else {
                statusLabel.setText("Error declining request.");
                statusLabel.setForeground(Color.RED);
            }
        } else {
            statusLabel.setText("No request selected.");
            statusLabel.setForeground(Color.RED);
        }
    }

    // Custom cell renderer for modern table styling
    private static class ModernTableCellRenderer extends DefaultTableCellRenderer {
        private final Color EVEN_ROW_COLOR = new Color(240, 240, 240);
        private final Color ODD_ROW_COLOR = Color.WHITE;
        private final Color SELECTED_ROW_COLOR = new Color(189, 195, 199);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            if (isSelected) {
                setBackground(SELECTED_ROW_COLOR);
                setForeground(Color.BLACK);
            } else {
                setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
                setForeground(Color.BLACK);
            }

            setHorizontalAlignment(CENTER);
            return this;
        }
    }
}
