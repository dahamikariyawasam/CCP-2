package server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        try {
            int index = Integer.parseInt(request.getParameter("index"));
            if (cart != null && index >= 0 && index < cart.size()) {
                cart.remove(index);
                session.setAttribute("cart", cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("itemcart.jsp");
    }
}
