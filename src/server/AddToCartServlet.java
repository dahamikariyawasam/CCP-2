package server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        double discount = Double.parseDouble(request.getParameter("discount"));

        HttpSession session = request.getSession();
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean itemExists = false;

        for (Map<String, Object> item : cart) {
            if (item.get("name").equals(name)) {
                int currentQty = (int) item.get("quantity");
                item.put("quantity", currentQty + 1);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("name", name);
            newItem.put("price", price);
            newItem.put("discount", discount);
            newItem.put("quantity", 1);
            cart.add(newItem);
        }

        session.setAttribute("cart", cart);
        session.setAttribute("cartMsg", "Item added to cart successfully!");
        response.sendRedirect("home.jsp");
    }
}
