package service;

import util.SingletonDatabase;

import java.sql.*;
import java.util.*;

public class ProductService {
    private final Connection connection;

    public ProductService(SingletonDatabase db) {
        this.connection = db.getConnection();
    }

    // ✅ Add stock directly to the stock table (No product table insertion)
    public void addStock(int productId, String itemName, double price, int stockQuantity, int stockAlertLevel,
                         String batchDate, String expiryDate, int warehouseId) throws SQLException {
        String stockQuery = "INSERT INTO stock (product_id, item_name, price, quantity, stock_alert_level, batch_date, expiry_date, warehouse_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement stockStmt = connection.prepareStatement(stockQuery)) {
            stockStmt.setInt(1, productId);
            stockStmt.setString(2, itemName);
            stockStmt.setDouble(3, price);
            stockStmt.setInt(4, stockQuantity);
            stockStmt.setInt(5, stockAlertLevel);
            stockStmt.setString(6, batchDate);
            stockStmt.setString(7, expiryDate);
            stockStmt.setInt(8, warehouseId);
            stockStmt.executeUpdate();
        }
    }

    // ✅ Update stock quantity
    public void updateStockQuantity(int stockId, int quantity) throws SQLException {
        String query = "UPDATE products SET stock_quantity = stock_quantity + ?, updated_at = NOW() WHERE id = ?";
        System.out.println("Executing SQL Query: " + query);
        System.out.println("Parameters: stockId=" + stockId + ", quantity=" + quantity);
        executeUpdate(query, quantity, stockId);
    }

    // ✅ Execute an update query
    private void executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement stmt = prepareStatement(query, params)) {
            stmt.executeUpdate();
        }
    }

    // ✅ Prepare a statement with parameters
    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    // ✅ Retrieve all products from the products table
    public List<Map<String, Object>> getAllProducts() throws SQLException {
        String query = "SELECT id, name, price, discount FROM products";
        List<Map<String, Object>> products = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("id", rs.getInt("id"));
                product.put("name", rs.getString("name"));
                product.put("price", rs.getDouble("price"));
                product.put("discount", rs.getDouble("discount"));
                products.add(product);
            }
        }
        return products;
    }

    // ✅ Add or update stock in products table
    public void upsertProductStock(int productId, String name, double price, int quantity, int alertLevel, double discount) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM products WHERE id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, productId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Product exists → update quantity, price and discount
                    String updateQuery = "UPDATE products SET stock_quantity = stock_quantity + ?, price = ?, discount = ?, updated_at = NOW() WHERE id = ?";
                    executeUpdate(updateQuery, quantity, price, discount, productId);
                } else {
                    // Product does not exist → insert new product
                    String insertQuery = "INSERT INTO products (id, name, price, stock_quantity, stock_alert_level, discount, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, NOW())";
                    executeUpdate(insertQuery, productId, name, price, quantity, alertLevel, discount);
                }
            }
        }
    }
}
