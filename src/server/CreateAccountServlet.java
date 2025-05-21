package server;

import util.SingletonDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class CreateAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        // ✅ Safely handle null role parameter
        String role = request.getParameter("role");
        if (role == null || role.trim().isEmpty()) {
            role = "customer";  // Set default role
        } else {
            role = role.trim();
        }

        try (Connection conn = SingletonDatabase.getInstance().getConnection()) {

            // Check if username already exists
            String checkSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    response.sendRedirect(request.getContextPath() + "/createaccount.jsp?error=exists");
                    return;
                }
            }

            // Insert new user
            String insertSql = "INSERT INTO users (username, password, role, created_at) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, role);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("✅ New user created: " + username);
                    response.sendRedirect(request.getContextPath() + "/login.jsp?registered=1");
                } else {
                    System.out.println("❌ User creation failed");
                    response.sendRedirect(request.getContextPath() + "/createaccount.jsp?error=insert");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/createaccount.jsp?error=exception");
        }
    }
}
