import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JButton refreshButton, deleteButton, editButton, addButton, landOfficerPanelButton;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;

    // Store the reference to the LandOfficerPanel window
    private JFrame landOfficerFrame;

    public AdminPanel() {
        setLayout(new BorderLayout());

        // User Table setup
        userTableModel = new DefaultTableModel(new String[]{"Username", "Role"}, 0);
        userTable = new JTable(userTableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Log Text Area setup
        logTextArea = new JTextArea(15, 30);
        logTextArea.setEditable(false);
        logScrollPane = new JScrollPane(logTextArea);
        add(logScrollPane, BorderLayout.EAST);

        // Buttons
        JPanel buttonPanel = new JPanel();
        refreshButton = new JButton("Refresh");
        deleteButton = new JButton("Delete User");
        editButton = new JButton("Edit User");
        addButton = new JButton("Add User");
        landOfficerPanelButton = new JButton("Land Officer Panel");

        refreshButton.addActionListener(e -> refreshData());
        deleteButton.addActionListener(e -> deleteUser());
        editButton.addActionListener(e -> editUser());
        addButton.addActionListener(e -> addUser());
        landOfficerPanelButton.addActionListener(e -> showLandOfficerPanel());

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(addButton);
        buttonPanel.add(landOfficerPanelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshData();
    }

    private void refreshData() {
        loadUsers();
        loadLogs();
    }

    private void loadUsers() {
        userTableModel.setRowCount(0); // Clear existing data

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "SELECT username, role FROM users";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String username = rs.getString("username");
                    String role = rs.getString("role");
                    userTableModel.addRow(new Object[]{username, role});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadLogs() {
        logTextArea.setText(""); // Clear existing text

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "SELECT timestamp, admin_username, action FROM logs ORDER BY timestamp DESC";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    String adminUsername = rs.getString("admin_username");
                    String action = rs.getString("action");
                    logTextArea.append(String.format("%s | %s | %s\n", timestamp, adminUsername, action));
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

        String username = (String) userTableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                refreshData(); // Refresh users and logs after deletion
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

        String username = (String) userTableModel.getValueAt(selectedRow, 0);
        String newUsername = JOptionPane.showInputDialog(this, "Enter new username for " + username + ":", "Edit User", JOptionPane.PLAIN_MESSAGE);
        String newRole = (String) JOptionPane.showInputDialog(
                this,
                "Select new role for " + username + ":",
                "Edit User",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"PublicUser", "LandOfficer", "Admin"},
                (String) userTableModel.getValueAt(selectedRow, 1)
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
                refreshData(); // Refresh users and logs after edit
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
                    refreshData(); // Refresh users and logs after adding
                    logActivity("User added: " + username + " with role " + role);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showLandOfficerPanel() {
        // Check if the frame is already open
        if (landOfficerFrame != null && landOfficerFrame.isShowing()) {
            landOfficerFrame.toFront(); // Bring the frame to the front
            return;
        }

        // Create and show the LandOfficerPanel frame
        landOfficerFrame = new JFrame("Land Officer Panel");
        landOfficerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        landOfficerFrame.setSize(400, 300);
        landOfficerFrame.add(new LandOfficerPanel());
        landOfficerFrame.setVisible(true);
    }

    private void logActivity(String message) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/land_management", "root", "")) {
            String sql = "INSERT INTO logs (timestamp, admin_username, action) VALUES (NOW(), ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "admin"); // Update this if necessary
                stmt.setString(2, message);
                stmt.executeUpdate();
                loadLogs();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
