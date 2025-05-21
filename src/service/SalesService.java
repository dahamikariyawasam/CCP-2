package service;

import util.SingletonDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalesService {
    private final Connection connection;

    public SalesService(SingletonDatabase db) {
        this.connection = db.getConnection();
    }

    // ✅ Insert sales data when a product is sold
    public void recordSale(int productId, int userId, int quantity, double totalAmount, String paymentType, String paymentStatus) throws SQLException {
        String query = "INSERT INTO sales (product_id, user_id, quantity, total_amount, payment_type, payment_status, sale_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, userId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalAmount);
            stmt.setString(5, paymentType);
            stmt.setString(6, paymentStatus);
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
        }
    }

    // ✅ Retrieve total sales for a given date
    public List<Map<String, Object>> getTotalSalesByDate(String date) throws SQLException {
        String query = "SELECT p.id AS item_code, p.name AS item_name, SUM(s.quantity) AS total_quantity, " +
                "(SUM(s.quantity) * p.price) AS total_revenue " + // ✅ Correct total revenue calculation
                "FROM sales s " +
                "JOIN products p ON s.product_id = p.id " +
                "WHERE DATE(s.sale_date) = ? " +
                "GROUP BY p.id, p.name, p.price " + // ✅ Grouping by price for correct revenue calculation
                "ORDER BY s.sale_date ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, date);
            System.out.println("Executing Sales Report Query for date: " + date); // Debugging

            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> salesList = new ArrayList<>();

                while (rs.next()) {
                    Map<String, Object> saleData = new LinkedHashMap<>();
                    saleData.put("Item Code", rs.getInt("item_code"));
                    saleData.put("Item Name", rs.getString("item_name"));
                    saleData.put("Total Quantity", rs.getInt("total_quantity"));
                    saleData.put("Total Revenue", rs.getDouble("total_revenue")); // ✅ Correct revenue calculation

                    System.out.println("Sale Data: " + saleData); // Debugging Output
                    salesList.add(saleData);
                }
                return salesList;
            }
        }
    }


    public List<Map<String, Object>> getReshelvingReport(String date) throws SQLException {
        String query = "SELECT p.id AS item_code, p.name AS item_name, SUM(s.quantity) AS total_sold " +
                "FROM sales s " +
                "JOIN products p ON s.product_id = p.id " +
                "WHERE DATE(s.sale_date) = ? " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_sold DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, date);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> reshelvingList = new ArrayList<>();

                while (rs.next()) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("Item Code", rs.getInt("item_code"));
                    item.put("Item Name", rs.getString("item_name"));
                    item.put("Total Sold", rs.getInt("total_sold"));
                    reshelvingList.add(item);
                }

                return reshelvingList;
            }
        }
    }




    // ✅ Retrieve all sales data
    public List<Map<String, Object>> getAllSales() throws SQLException {
        String query = "SELECT s.id AS sale_id, p.id AS item_code, p.name AS item_name, s.quantity, s.total_amount, " +
                "s.payment_type, s.payment_status, s.sale_date " +
                "FROM sales s " +
                "JOIN products p ON s.product_id = p.id " +
                "ORDER BY s.sale_date ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> salesList = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> saleData = new LinkedHashMap<>();
                saleData.put("Sale ID", rs.getInt("sale_id"));
                saleData.put("Item Code", rs.getInt("item_code"));
                saleData.put("Item Name", rs.getString("item_name"));
                saleData.put("Quantity", rs.getInt("quantity"));
                saleData.put("Total Amount", String.format("Rs. %.2f", rs.getDouble("total_amount")));
                saleData.put("Payment Type", rs.getString("payment_type"));
                saleData.put("Payment Status", rs.getString("payment_status"));
                saleData.put("Sale Date", rs.getString("sale_date"));
                salesList.add(saleData);
            }
            return salesList;
        }
    }

    // ✅ Retrieve sales data by product ID
    public List<Map<String, Object>> getSalesByProductId(int productId) throws SQLException {
        String query = "SELECT s.id AS sale_id, p.id AS item_code, p.name AS item_name, s.quantity, s.total_amount, " +
                "s.payment_type, s.payment_status, s.sale_date " +
                "FROM sales s " +
                "JOIN products p ON s.product_id = p.id " +
                "WHERE p.id = ? " +
                "ORDER BY s.sale_date ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> salesList = new ArrayList<>();

                while (rs.next()) {
                    Map<String, Object> saleData = new LinkedHashMap<>();
                    saleData.put("Sale ID", rs.getInt("sale_id"));
                    saleData.put("Item Code", rs.getInt("item_code"));
                    saleData.put("Item Name", rs.getString("item_name"));
                    saleData.put("Quantity", rs.getInt("quantity"));
                    saleData.put("Total Amount", String.format("Rs. %.2f", rs.getDouble("total_amount")));
                    saleData.put("Payment Type", rs.getString("payment_type"));
                    saleData.put("Payment Status", rs.getString("payment_status"));
                    saleData.put("Sale Date", rs.getString("sale_date"));
                    salesList.add(saleData);
                }
                return salesList;
            }
        }
    }
}