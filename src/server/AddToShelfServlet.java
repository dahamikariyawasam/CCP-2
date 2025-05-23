package server;

import service.ProductService;
import util.SingletonDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/add-to-shelf")
public class AddToShelfServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // ✅ Get form inputs
            int productId = Integer.parseInt(request.getParameter("productId"));
            String productName = request.getParameter("productName");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double price = Double.parseDouble(request.getParameter("price"));
            int alertLevel = Integer.parseInt(request.getParameter("alertLevel"));
            double discount = Double.parseDouble(request.getParameter("discount"));

            // ✅ Create ProductService instance
            ProductService productService = new ProductService(SingletonDatabase.getInstance());

            // ✅ Add or update the product in products table
            productService.upsertProductStock(productId, productName, price, quantity, alertLevel, discount);

            // ✅ Reduce stock from warehouse
            String updateWarehouse = "UPDATE warehouse SET quantity = quantity - ?, updated_at = NOW() " +
                    "WHERE product_id = ? AND quantity >= ?";
            try (var conn = SingletonDatabase.getInstance().getConnection();
                 var stmt = conn.prepareStatement(updateWarehouse)) {

                stmt.setInt(1, quantity);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected == 0) {
                    throw new IllegalArgumentException("Not enough stock in warehouse or product not found.");
                }
            }

            response.sendRedirect("addshelf.jsp?success=Stock+successfully+added+to+product+shelf");

        } catch (NumberFormatException e) {
            response.sendRedirect("addshelf.jsp?error=Invalid+input+format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addshelf.jsp?error=" + e.getMessage().replace(" ", "+"));
        }
    }
}
