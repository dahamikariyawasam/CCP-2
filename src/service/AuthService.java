package service;

import util.SingletonDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private Connection connection;

    // ✅ Ensure connection is always active
    public AuthService() {
        reconnect();
    }

    // ✅ Validate user credentials
    public boolean validateUser(String username, String password) throws SQLException {
        reconnect(); // ✅ Ensure connection is active

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error during authentication: " + e.getMessage());
            throw e; // ✅ Ensure the exception is not silently handled
        }
    }


    // ✅ Ensure connection is always open
    private void reconnect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = SingletonDatabase.getInstance().getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
