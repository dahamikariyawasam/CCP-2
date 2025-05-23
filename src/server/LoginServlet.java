package server;

import service.AuthService;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // ✅ Check for hardcoded admin
        if ("admin".equalsIgnoreCase(username) && "1234".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", "admin");
            session.setAttribute("role", "admin");
            response.sendRedirect(request.getContextPath() + "/admindashboard.jsp");
            return;
        }

        // ✅ Use AuthService with SQLException handling
        AuthService authService = new AuthService();

        try {
            boolean isValidUser = authService.validateUser(username, password);

            if (isValidUser) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", "user"); // default role since role fetching is skipped
                response.sendRedirect(request.getContextPath() + "/home.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=db");
        }
    }
}
