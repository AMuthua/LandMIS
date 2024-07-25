//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class Main {
//    private static final String URL = "jdbc:mysql://localhost:3306/land_management";
//    private static final String USER = "root"; // Replace with your database username
//    private static final String PASSWORD = ""; // Your database password is empty
//
//    public static void main(String[] args) {
//        try {
//            // Load the MySQL JDBC driver
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // Establish a connection to the database
//            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//            System.out.println("Database connected successfully.");
//
//            // Close the connection
//            connection.close();
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}


public class Main {
    public static void main(String[] args) {
        // Initialize and display the login screen
        new Login();
    }
}
