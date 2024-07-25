import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandOfficer {
    // Method to display the Land Officer panel
    public JPanel createLandOfficerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create buttons for Land Officer responsibilities
        JButton registerLandButton = new JButton("Register Land");
        JButton processTransactionsButton = new JButton("Process Transactions");
        JButton conductSurveysButton = new JButton("Conduct Surveys");
        JButton handleComplaintsButton = new JButton("Handle Complaints");
        JButton manageRecordsButton = new JButton("Manage Records");

        // Add action listeners
        registerLandButton.addActionListener(e -> registerLand());
        processTransactionsButton.addActionListener(e -> processTransactions());
        conductSurveysButton.addActionListener(e -> conductSurveys());
        handleComplaintsButton.addActionListener(e -> handleComplaints());
        manageRecordsButton.addActionListener(e -> manageRecords());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        buttonPanel.add(registerLandButton);
        buttonPanel.add(processTransactionsButton);
        buttonPanel.add(conductSurveysButton);
        buttonPanel.add(handleComplaintsButton);
        buttonPanel.add(manageRecordsButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    // Implementations for Land Officer responsibilities
    private void registerLand() {
        JOptionPane.showMessageDialog(null, "Land registration functionality to be implemented.");
    }

    private void processTransactions() {
        JOptionPane.showMessageDialog(null, "Land transaction processing functionality to be implemented.");
    }

    private void conductSurveys() {
        JOptionPane.showMessageDialog(null, "Conducting surveys functionality to be implemented.");
    }

    private void handleComplaints() {
        JOptionPane.showMessageDialog(null, "Handling complaints functionality to be implemented.");
    }

    private void manageRecords() {
        JOptionPane.showMessageDialog(null, "Managing records functionality to be implemented.");
    }
}
