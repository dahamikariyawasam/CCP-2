package server;

import util.SingletonDatabase;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")  // ✅ Maps /login to this servlet
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("🚀 LoginServlet triggered");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = SingletonDatabase.getInstance().getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("role", rs.getString("role"));

                    System.out.println("✅ Login successful for: " + username);

                    // ✅ Correct redirect: no /web/ prefix
                    response.sendRedirect(request.getContextPath() + "/home.jsp");

                } else {
                    System.out.println("❌ Invalid login attempt: " + username);
                    response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
        }
    }
}
