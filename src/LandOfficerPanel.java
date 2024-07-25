import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LandOfficerPanel extends JPanel {
    private JButton reviewApplicationsButton, fieldInspectionsButton, generateReportsButton, updateRecordsButton, viewOwnershipButton;

    public LandOfficerPanel() {
        setLayout(new GridLayout(5, 1, 10, 10));

        reviewApplicationsButton = new JButton("Review Land Applications");
        fieldInspectionsButton = new JButton("Conduct Field Inspections");
        generateReportsButton = new JButton("Generate Land Reports");
        updateRecordsButton = new JButton("Update Land Records");
        viewOwnershipButton = new JButton("View Land Ownership Details");

        reviewApplicationsButton.addActionListener(e -> reviewLandApplications());
        fieldInspectionsButton.addActionListener(e -> conductFieldInspections());
        generateReportsButton.addActionListener(e -> generateLandReports());
        updateRecordsButton.addActionListener(e -> updateLandRecords());
        viewOwnershipButton.addActionListener(e -> viewLandOwnershipDetails());

        add(reviewApplicationsButton);
        add(fieldInspectionsButton);
        add(generateReportsButton);
        add(updateRecordsButton);
        add(viewOwnershipButton);
    }

    private void reviewLandApplications() {
        JOptionPane.showMessageDialog(this, "Review Land Applications clicked.");
        logActivity("Reviewed land applications.");
    }

    private void conductFieldInspections() {
        JOptionPane.showMessageDialog(this, "Conduct Field Inspections clicked.");
        logActivity("Conducted field inspections.");
    }

    private void generateLandReports() {
        JOptionPane.showMessageDialog(this, "Generate Land Reports clicked.");
        logActivity("Generated land reports.");
    }

    private void updateLandRecords() {
        JOptionPane.showMessageDialog(this, "Update Land Records clicked.");
        logActivity("Updated land records.");
    }

    private void viewLandOwnershipDetails() {
        JOptionPane.showMessageDialog(this, "View Land Ownership Details clicked.");
        logActivity("Viewed land ownership details.");
    }

    private void logActivity(String message) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "INSERT INTO logs (timestamp, admin_username, action) VALUES (NOW(), ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "land_officer"); // Update this if necessary
                stmt.setString(2, message);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
