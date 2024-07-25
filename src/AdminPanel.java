import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton, deleteButton, editButton, addButton, switchToLandOfficerButton;
    private LandOfficer landOfficerPanel;

    public AdminPanel() {
        setLayout(new BorderLayout());

        // Initialize Land Officer
        landOfficerPanel = new LandOfficer();

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Username", "Role"}, 0);
        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete User");
        editButton = new JButton("Edit User");
        addButton = new JButton("Add User");
        switchToLandOfficerButton = new JButton("Land Officer Panel");

        refreshButton.addActionListener(e -> loadUsers());
        deleteButton.addActionListener(e -> deleteUser());
        editButton.addActionListener(e -> editUser());
        addButton.addActionListener(e -> addUser());
        switchToLandOfficerButton.addActionListener(e -> switchToLandOfficer());

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(addButton);
        buttonPanel.add(switchToLandOfficerButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        loadUsers();
    }

    private void loadUsers() {
        tableModel.setRowCount(0); // Clear existing data

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "SELECT username, role FROM users";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String username = rs.getString("username");
                    String role = rs.getString("role");
                    tableModel.addRow(new Object[]{username, role});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to delete.");
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                loadUsers();
                logActivity("User deleted: " + username);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to edit.");
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        String newUsername = JOptionPane.showInputDialog(this, "Enter new username for " + username + ":", "Edit User", JOptionPane.PLAIN_MESSAGE);
        String newRole = (String) JOptionPane.showInputDialog(
                this,
                "Select new role for " + username + ":",
                "Edit User",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"PublicUser", "LandOfficer", "Admin"},
                (String) tableModel.getValueAt(selectedRow, 1)
        );

        if (newUsername == null || newUsername.trim().isEmpty() || newRole == null) {
            JOptionPane.showMessageDialog(this, "Username and role cannot be empty.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "UPDATE users SET username = ?, role = ? WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, newUsername);
                stmt.setString(2, newRole);
                stmt.setString(3, username);
                stmt.executeUpdate();
                loadUsers();
                logActivity("User updated: " + username + " to " + newUsername + " with role " + newRole);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addUser() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"PublicUser", "LandOfficer", "Admin"});

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                JOptionPane.showMessageDialog(this, "Username, password, and role cannot be empty.");
                return;
            }

            String hashedPassword = DatabaseUtils.hashPassword(password);

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashedPassword);
                    stmt.setString(3, role);
                    stmt.executeUpdate();
                    loadUsers();
                    logActivity("User added: " + username + " with role " + role);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void switchToLandOfficer() {
        // Display the Land Officer panel
        JPanel landOfficerPanel = new LandOfficer().createLandOfficerPanel();
        JFrame landOfficerFrame = new JFrame("Land Officer Panel");
        landOfficerFrame.setSize(400, 300);
        landOfficerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        landOfficerFrame.add(landOfficerPanel);
        landOfficerFrame.setLocationRelativeTo(null);
        landOfficerFrame.setVisible(true);
    }

    private void logActivity(String message) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "INSERT INTO logs (timestamp, admin_username, action) VALUES (NOW(), ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "admin"); // Replace with the current admin username if applicable
                stmt.setString(2, message);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
