package Coinzy.views.admin.sections;

import org.knowm.xchart.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.style.Styler;
import Coinzy.controllers.admin.OverviewController;

public class OverviewPanel extends JPanel {
    private JLabel totalUsersLabel;
    private JLabel pendingRequestsLabel;
    private JLabel revenueLabel;
    private JLabel expensesLabel;
    private JLabel rejectedUsersLabel;
    private JLabel blockedUsersLabel;

    private final OverviewController overviewController;
    private JPanel chartPanel;
    private JPanel statsPanel;

    public OverviewPanel() {
        this.overviewController = new OverviewController();
        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create a scroll pane to make the panel scrollable
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create top panel for chart
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.add(createChartPanel(), BorderLayout.CENTER);

        // Create bottom panel for statistics cards
        statsPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // Set 3 columns, dynamic row count
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around grid

        // Create and add statistic cards
        addCard("Total Users", totalUsersLabel = createStatLabel(), "Users", statsPanel);
        addCard("Pending Requests", pendingRequestsLabel = createStatLabel(), "Requests", statsPanel);
        addCard("Rejected Users", rejectedUsersLabel = createStatLabel(), "RejectedUsers", statsPanel);
        addCard("Blocked Users", blockedUsersLabel = createStatLabel(), "BlockedUsers", statsPanel);
        addCard("Total Revenue", revenueLabel = createStatLabel(), "Revenue", statsPanel);
        addCard("Total Expenses", expensesLabel = createStatLabel(), "Expenses", statsPanel);

        // Create refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());

        // Create a panel to hold the refresh button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);

        // Add chart, stats, and button panel to the main panel
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void addCard(String titleText, JLabel statLabel, String iconName, JPanel parentPanel) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(new RoundedBorder(10, Color.LIGHT_GRAY)); // Rounded border
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(150, 80)); // Adjusted smaller size for the cards

        // Icon for the card
        ImageIcon icon = new ImageIcon("resources/icons/" + iconName + ".png");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Adjust font size to fit smaller card
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text

        // Center the text and icon in the card
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.add(titleLabel);
        textPanel.add(statLabel);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding inside text panel

        cardPanel.add(iconLabel, BorderLayout.WEST);
        cardPanel.add(textPanel, BorderLayout.CENTER);

        parentPanel.add(cardPanel); // Add the card to the parent panel
    }

    private void loadStatistics() {
        // Fetch statistics from the controller and set them in the labels
        totalUsersLabel.setText(String.valueOf(overviewController.getTotalUsers()));
        pendingRequestsLabel.setText(String.valueOf(overviewController.getPendingRequests()));
        rejectedUsersLabel.setText(String.valueOf(overviewController.getRejectedUsersCount()));
        blockedUsersLabel.setText(String.valueOf(overviewController.getBlockedUsersCount()));
        revenueLabel.setText(formatCurrency(overviewController.getTotalRevenue()));
        expensesLabel.setText(formatCurrency(overviewController.getTotalExpenses()));
    }

    private void refreshData() {
        // Reload statistics and chart data
        loadStatistics();
        chartPanel.removeAll();
        chartPanel.add(createChartPanel(), BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    // Create a label for displaying statistics
    private JLabel createStatLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Modern font for stats
        label.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        return label;
    }

    // Format currency values for revenue and expenses
    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        return currencyFormat.format(amount);
    }

    private JPanel createChartPanel() {
        // Fetch the user growth data aggregated by month from the controller
        Map<String, Integer> userGrowth = overviewController.getUserGrowthByMonth();

        // Create lists for month labels and user counts
        List<String> months = new ArrayList<>(userGrowth.keySet());
        List<Integer> userCounts = new ArrayList<>(userGrowth.values());

        // Create chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(550)
                .height(400)
                .title("User Growth Over Time")
                .xAxisTitle("Month")
                .yAxisTitle("Users")
                .build();

        // Configure chart style
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setXAxisLabelRotation(45); // Rotate X-axis labels for better readability

        // Add series with aggregated monthly user growth data
        chart.addSeries("User Growth", months, userCounts);

        // Return chart panel
        return new XChartPanel<>(chart);
    }

    // Custom border class for rounded borders
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }
    }
}
