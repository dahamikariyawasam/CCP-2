package server;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/PlaceOrderServlet")
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Collect form data
        String customerName = request.getParameter("customerName");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");
        String totalAmount = request.getParameter("totalAmount");

        HttpSession session = request.getSession();
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("checkout.jsp?error=empty");
            return;
        }

        // Step 2: (Optional) Save order to DB
        // — this step is skipped here, but can be added using JDBC

        // Step 3: Clear cart
        session.removeAttribute("cart");

        // Step 4: Redirect to confirmation
        session.setAttribute("orderDetails", Map.of(
                "customerName", customerName,
                "address", address,
                "paymentMethod", paymentMethod,
                "totalAmount", totalAmount
        ));
        response.sendRedirect("success.jsp");
    }
}
