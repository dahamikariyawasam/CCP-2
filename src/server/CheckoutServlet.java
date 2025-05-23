package server;

import service.BillService;
import service.ProductService;
import service.SalesService;
import util.SingletonDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("checkout.jsp?error=Cart%20is%20empty");
            return;
        }

        double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        double cashTendered = Double.parseDouble(request.getParameter("cash"));

        if (cashTendered < totalAmount) {
            response.sendRedirect("checkout.jsp?error=Insufficient%20cash");
            return;
        }

        double change = cashTendered - totalAmount;

        try {
            Connection conn = SingletonDatabase.getInstance().getConnection();
            conn.setAutoCommit(false); // ✅ Begin transaction

            BillService billService = new BillService(SingletonDatabase.getInstance());
            ProductService productService = new ProductService(SingletonDatabase.getInstance());
            SalesService salesService = new SalesService(SingletonDatabase.getInstance());

            int serialNumber = billService.generateSerialNumber();
            int userId = 1; // Default or fetched from session if needed

            for (Map<String, Object> item : cart) {
                String itemName = (String) item.get("name");
                double price = (Double) item.get("price");
                double discount = (Double) item.get("discount");
                int quantity = (int) item.get("quantity");

                double fullPrice = price * quantity;
                double totalPrice = fullPrice * (1 - discount / 100);
                int productId = productIdByName(conn, itemName);

                // ✅ Save bill item
                boolean billSaved = billService.saveBill(serialNumber, itemName, quantity, fullPrice, discount, totalPrice, cashTendered, change);
                if (!billSaved) {
                    conn.rollback();
                    response.sendRedirect("checkout.jsp?error=Failed%20to%20save%20bill");
                    return;
                }

                // ✅ Update stock
                productService.updateStockQuantity(productId, -quantity);

                // ✅ Save sales record
                salesService.recordSale(productId, userId, quantity, totalPrice, "Cash", "Paid");
            }

            conn.commit();

            // ✅ Pass bill info to session for bill.jsp
            session.setAttribute("billCart", cart);
            session.setAttribute("billCash", cashTendered);
            session.setAttribute("billChange", change);
            session.setAttribute("billSerial", serialNumber);
            session.setAttribute("billDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            session.removeAttribute("cart");
            response.sendRedirect("bill.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("checkout.jsp?error=Database%20Error:%20" + e.getMessage().replace(" ", "%20"));
        }
    }

    // ✅ Helper to get product ID by name
    private int productIdByName(Connection conn, String itemName) throws SQLException {
        String sql = "SELECT id FROM products WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Product not found: " + itemName);
    }
}
