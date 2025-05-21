package service;

import util.SingletonDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    private final Connection connection;

    public UserService(SingletonDatabase db) {
        this.connection = db.getConnection();
    }

    public boolean createUser(String username, String password, String role) throws SQLException {
        // ✅ Validate input before executing SQL (Prevents invalid data)
        if (username == null || username.trim().isEmpty()) {
            throw new SQLException("Username cannot be empty!");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new SQLException("Password cannot be empty!");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new SQLException("Role cannot be empty!");
        }

        String query = "INSERT INTO users (username, password, role, created_at) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, hash passwords
            stmt.setString(3, role);
            return stmt.executeUpdate() > 0;
        }
    }
}
