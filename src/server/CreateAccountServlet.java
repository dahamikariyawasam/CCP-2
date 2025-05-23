package server;

import service.UserService;
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

        // ✅ Handle missing role
        String role = request.getParameter("role");
        if (role == null || role.trim().isEmpty()) {
            role = "customer";
        } else {
            role = role.trim();
        }

        try (Connection conn = SingletonDatabase.getInstance().getConnection()) {

            // ✅ Check for existing user
            String checkSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    response.sendRedirect(request.getContextPath() + "/createaccount.jsp?error=exists");
                    return;
                }
            }

            // ✅ Call UserService to insert user
            UserService userService = new UserService(SingletonDatabase.getInstance());
            boolean success = userService.createUser(username, password, role);

            if (success) {
                System.out.println("✅ New user created: " + username);
                response.sendRedirect(request.getContextPath() + "/login.jsp?registered=1");
            } else {
                System.out.println("❌ User creation failed");
                response.sendRedirect(request.getContextPath() + "/createaccount.jsp?error=insert");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/createaccount.jsp?error=exception");
        }
    }
}
