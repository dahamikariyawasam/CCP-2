package server;

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

        try (Connection conn = SingletonDatabase.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // ✅ Get next serial number
            int serialNumber = 1;
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT MAX(serial_number) FROM bills")) {
                if (rs.next()) {
                    serialNumber = rs.getInt(1) + 1;
                }
            }

            for (Map<String, Object> item : cart) {
                String itemName = (String) item.get("name");
                double price = (Double) item.get("price");
                double discount = (Double) item.get("discount");
                int quantity = (int) item.get("quantity");

                double fullPrice = price * quantity;
                double totalPrice = fullPrice * (1 - discount / 100);

                // ✅ Insert into bills table
                String sql = "INSERT INTO bills (serial_number, bill_date, item_name, quantity, full_price, total_price, discount, cash_tendered, change_amount) " +
                        "VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, serialNumber);
                    stmt.setString(2, itemName);
                    stmt.setInt(3, quantity);
                    stmt.setDouble(4, fullPrice);
                    stmt.setDouble(5, totalPrice);
                    stmt.setDouble(6, discount);
                    stmt.setDouble(7, cashTendered);
                    stmt.setDouble(8, change);
                    stmt.executeUpdate();
                }

                // ✅ Update stock
                String updateQty = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE name = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQty)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, itemName);
                    int affectedRows = updateStmt.executeUpdate();
                    if (affectedRows == 0) {
                        conn.rollback();
                        response.sendRedirect("checkout.jsp?error=Product%20not%20found:%20" + itemName);
                        return;
                    }
                }
            }

            conn.commit();

            // ✅ Pass data to bill.jsp
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
}
