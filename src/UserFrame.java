import javax.swing.*;
import java.awt.*;

public class UserFrame extends JFrame {
    private User user;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public UserFrame(User user) {
        this.user = user;
        setTitle("Land Management System - " + user.getRole());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        cardLayout = new CardLayout();

        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.add(createAdminPanel(), "Admin");
        contentPanel.add(createLandOfficerPanel(), "LandOfficer");
        contentPanel.add(createGeneralPublicPanel(), "PublicUser");

        mainPanel.add(new JLabel("Welcome, " + user.getUsername() + "!", JLabel.CENTER), BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new Login();
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        add(mainPanel);

        // Show the panel based on the user's role
        switch (user.getRole()) {
            case "Admin":
                cardLayout.show(contentPanel, "Admin");
                break;
            case "LandOfficer":
                cardLayout.show(contentPanel, "LandOfficer");
                break;
            case "PublicUser":
                cardLayout.show(contentPanel, "PublicUser");
                break;
            default:
                cardLayout.show(contentPanel, "PublicUser");
                break;
        }
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton manageUsersButton = new JButton("Manage Users");
        manageUsersButton.addActionListener(e -> manageUsers());

        JButton generateReportsButton = new JButton("Generate Reports");
        generateReportsButton.addActionListener(e -> generateReports());

        panel.add(manageUsersButton);
        panel.add(generateReportsButton);
        return panel;
    }

    private void manageUsers() {
        JFrame adminFrame = new JFrame("Manage Users");
        adminFrame.setSize(600, 400);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        AdminPanel adminPanel = new AdminPanel();
        adminFrame.add(adminPanel);
        adminFrame.setVisible(true);
    }

    private void generateReports() {
        JOptionPane.showMessageDialog(this, "Generate Reports clicked");
    }

    private JPanel createLandOfficerPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton conductSearchButton = new JButton("Conduct Land Search");
        conductSearchButton.addActionListener(e -> conductLandSearch());

        JButton scheduleInspectionButton = new JButton("Schedule Inspection");
        scheduleInspectionButton.addActionListener(e -> scheduleInspection());

        JButton processTransferButton = new JButton("Process Transfer");
        processTransferButton.addActionListener(e -> processTransfer());

        JButton viewLandOfficerActivitiesButton = new JButton("View Land Officer Activities");
        viewLandOfficerActivitiesButton.addActionListener(e -> viewLandOfficerActivities());

        panel.add(conductSearchButton);
        panel.add(scheduleInspectionButton);
        panel.add(processTransferButton);
        panel.add(viewLandOfficerActivitiesButton);
        return panel;
    }

    private JPanel createGeneralPublicPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton searchLandButton = new JButton("Search Land");
        searchLandButton.addActionListener(e -> searchLand());

        JButton submitApplicationButton = new JButton("Submit Application");
        submitApplicationButton.addActionListener(e -> submitApplication());

        JButton trackStatusButton = new JButton("Track Application Status");
        trackStatusButton.addActionListener(e -> trackApplicationStatus());

        panel.add(searchLandButton);
        panel.add(submitApplicationButton);
        panel.add(trackStatusButton);
        return panel;
    }

    private void conductLandSearch() {
        JOptionPane.showMessageDialog(this, "Conduct Land Search clicked");
    }

    private void scheduleInspection() {
        JOptionPane.showMessageDialog(this, "Schedule Inspection clicked");
    }

    private void processTransfer() {
        JOptionPane.showMessageDialog(this, "Process Transfer clicked");
    }

    private void viewLandOfficerActivities() {
        // Code to view Land Officer activities
        JOptionPane.showMessageDialog(this, "View Land Officer Activities clicked");
    }

    private void searchLand() {
        JOptionPane.showMessageDialog(this, "Search Land clicked");
    }

    private void submitApplication() {
        JOptionPane.showMessageDialog(this, "Submit Application clicked");
    }

    private void trackApplicationStatus() {
        JOptionPane.showMessageDialog(this, "Track Application Status clicked");
    }
}
