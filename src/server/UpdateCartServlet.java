package server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart != null) {
            for (int i = 0; i < cart.size(); i++) {
                String param = request.getParameter("quantity_" + i);
                if (param != null) {
                    try {
                        int newQty = Integer.parseInt(param);
                        if (newQty > 0) {
                            cart.get(i).put("quantity", newQty);
                        }
                    } catch (NumberFormatException e) {
                        // ignore invalid input
                    }
                }
            }
            session.setAttribute("cart", cart);
        }

        response.sendRedirect("itemcart.jsp");
    }
}
