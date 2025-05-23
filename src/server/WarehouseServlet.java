package server;

import service.WarehouseService;
import util.SingletonDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/warehouse")
public class WarehouseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        WarehouseService warehouseService = new WarehouseService(SingletonDatabase.getInstance());

        try {
            String action = request.getParameter("action");

            if ("add".equalsIgnoreCase(action)) {
                // Add or update warehouse item
                String pidStr = request.getParameter("productId");
                String name = request.getParameter("itemName");
                String batchDate = request.getParameter("batchDate");
                String expiryDate = request.getParameter("expiryDate");
                String quantityStr = request.getParameter("quantity");
                String widStr = request.getParameter("warehouseId");

                if (pidStr == null || quantityStr == null || widStr == null ||
                        name == null || batchDate == null || expiryDate == null ||
                        pidStr.isEmpty() || quantityStr.isEmpty() || widStr.isEmpty() ||
                        name.isEmpty() || batchDate.isEmpty() || expiryDate.isEmpty()) {
                    throw new IllegalArgumentException("Missing input data");
                }

                int productId = Integer.parseInt(pidStr);
                int quantity = Integer.parseInt(quantityStr);
                int warehouseId = Integer.parseInt(widStr);

                warehouseService.addOrUpdateWarehouseStock(productId, name, batchDate, expiryDate, quantity, warehouseId);
                response.sendRedirect("warehouse.jsp?success=add");

            } else if ("update".equalsIgnoreCase(action)) {
                // Update only quantity by product ID
                String pidStr = request.getParameter("productId");
                String quantityStr = request.getParameter("quantity");

                if (pidStr == null || quantityStr == null || pidStr.isEmpty() || quantityStr.isEmpty()) {
                    throw new IllegalArgumentException("Missing input for quantity update");
                }

                int productId = Integer.parseInt(pidStr);
                int quantity = Integer.parseInt(quantityStr);

                warehouseService.updateQuantityByProductId(productId, quantity);
                response.sendRedirect("warehouse.jsp?success=update");

            } else {
                response.sendRedirect("warehouse.jsp?error=Invalid%20action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("warehouse.jsp?error=" + e.getMessage().replace(" ", "%20"));
        }
    }
}
