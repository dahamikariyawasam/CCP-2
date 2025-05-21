package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonDatabase {
    private static SingletonDatabase instance;
    private Connection connection;

    private final String URL = "jdbc:mysql://localhost:3306/ccpdb";  // ✅ Replace with your DB name
    private final String USER = "root";
    private final String PASSWORD = "";

    private SingletonDatabase() {
        try {
            // Load the JDBC driver (optional in newer versions, but safe to keep)
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to the database successfully!");
        } catch (SQLException e) {
            System.err.println("❌ SQL Exception: " + e.getMessage());
            throw new RuntimeException("Database connection failed!", e);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found!");
            throw new RuntimeException("JDBC Driver loading failed!", e);
        }
    }

    public static SingletonDatabase getInstance() {
        if (instance == null) {
            instance = new SingletonDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error reconnecting to the database", e);
        }
        return connection;
    }
}
