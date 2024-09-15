package Coinzy.views.admin.sections;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Coinzy.controllers.admin.AnnouncementController;
import Coinzy.models.Announcement;

public class AnnouncementPanel extends JPanel {
    private JTable announcementTable;
    private final AnnouncementController announcementController;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea messageField;

    public AnnouncementPanel() {
        this.announcementController = new AnnouncementController();
        initComponents();
        loadAnnouncements();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Split the panel into two sections: one for adding and one for displaying
        // announcements
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(150); // Set the initial divider location
        splitPane.setDividerSize(0); // Make the divider invisible
        splitPane.setEnabled(false); // Disable user resizing

        // Section 1: Announcement creation form
        JPanel createAnnouncementPanel = new JPanel();
        createAnnouncementPanel.setLayout(new GridBagLayout());
        createAnnouncementPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add margin between components
        gbc.fill = GridBagConstraints.BOTH; // Make components resizeable
        gbc.weightx = 1.0; // Allow fields to expand
        gbc.gridy = 0;

        // Title input
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        createAnnouncementPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        titleField = new JTextField();
        titleField.setFont(titleField.getFont().deriveFont(16f));
        titleField.setBorder(new EmptyBorder(5, 5, 5, 5));
        createAnnouncementPanel.add(titleField, gbc);

        // Message input
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        createAnnouncementPanel.add(new JLabel("Message:"), gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0; // Allow message field to expand
        messageField = new JTextArea(5, 30);
        messageField.setFont(messageField.getFont().deriveFont(16f));
        messageField.setLineWrap(true);
        messageField.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageField);
        messageScrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        createAnnouncementPanel.add(messageScrollPane, gbc);

        // Add Announcement button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.weightx = 0.0;
        JButton addButton = new JButton("Add Announcement");
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(addButton.getFont().deriveFont(16f));
        addButton.addActionListener(this::handleAddAnnouncement);
        createAnnouncementPanel.add(addButton, gbc);

        // Section 2: Announcement table
        JPanel announcementTablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[] { "ID", "Title", "Message", "Created By", "Timestamp" }, 0);
        announcementTable = new JTable(tableModel);
        customizeTable();

        JScrollPane tableScrollPane = new JScrollPane(announcementTable);
        tableScrollPane.setBorder(null); // Remove border if desired

        // Add padding to the table
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(new EmptyBorder(10, 20, 10, 20)); // Add padding
        tableWrapper.add(tableScrollPane, BorderLayout.CENTER);

        announcementTablePanel.add(tableWrapper, BorderLayout.CENTER);

        // Add panels to the split pane
        splitPane.setTopComponent(createAnnouncementPanel);
        splitPane.setBottomComponent(announcementTablePanel);

        // Add the split pane to the main panel
        JScrollPane scrollPane = new JScrollPane(splitPane);
        scrollPane.setBorder(null); // Remove border if desired
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadAnnouncements() {
        List<Announcement> announcements = announcementController.getAllAnnouncements();
        tableModel.setRowCount(0); // Clear existing data
        for (Announcement announcement : announcements) {
            tableModel.addRow(new Object[] {
                    announcement.getId(),
                    announcement.getTitle(),
                    announcement.getMessage(),
                    announcement.getCreatedBy(), // Now this will show the user's name
                    announcement.getTimestamp()
            });
        }
    }

    // Customize the table's look and feel
    private void customizeTable() {
        announcementTable.setRowHeight(30);
        announcementTable.getTableHeader().setReorderingAllowed(false);
        announcementTable.getTableHeader().setFont(announcementTable.getTableHeader().getFont().deriveFont(16f));
        announcementTable.setFont(announcementTable.getFont().deriveFont(14f));

        // Center-align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < announcementTable.getColumnCount(); i++) {
            announcementTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // Handle adding a new announcement
    private void handleAddAnnouncement(ActionEvent e) {
        String title = titleField.getText();
        String message = messageField.getText();

        if (!title.isEmpty() && !message.isEmpty()) {
            boolean success = announcementController.addAnnouncement(title, message);
            if (success) {
                JOptionPane.showMessageDialog(null, "Announcement added successfully!");
                loadAnnouncements();
                titleField.setText("");
                messageField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add announcement.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Title and message are required.");
        }
    }
}
