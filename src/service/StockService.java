package service;

import util.SingletonDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StockService {
    private final Connection connection;

    public StockService(SingletonDatabase db) {
        this.connection = db.getConnection();
    }

    // ✅ Add or update warehouse stock intelligently
    public boolean addOrUpdateWarehouseStock(int productId, String itemName, String batchDate,
                                             String expiryDate, int quantity, int warehouseId) throws SQLException {
        String checkQuery = "SELECT id, quantity FROM warehouse WHERE product_id = ? AND batch_date = ? AND expiry_date = ? AND warehouse_id = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, productId);
            checkStmt.setString(2, batchDate);
            checkStmt.setString(3, expiryDate);
            checkStmt.setInt(4, warehouseId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int existingId = rs.getInt("id");
                    String updateQuery = "UPDATE warehouse SET quantity = quantity + ?, updated_at = NOW() WHERE id = ?";
                    executeUpdate(updateQuery, quantity, existingId);
                } else {
                    String insertQuery = "INSERT INTO warehouse (product_id, item_name, batch_date, expiry_date, quantity, warehouse_id, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, NOW())";
                    executeUpdate(insertQuery, productId, itemName, batchDate, expiryDate, quantity, warehouseId);
                }
            }
        }
        return true;
    }

    // ✅ Move stock from warehouse to product shelf
    public void moveStockToProductShelf(int warehouseId, int productId, int quantity, double price, int stockAlertLevel) throws SQLException {
        String checkStockQuery = "SELECT item_name, quantity FROM warehouse WHERE warehouse_id = ? AND product_id = ?";
        String checkProductQuery = "SELECT id FROM products WHERE id = ?";
        String insertProductQuery = "INSERT INTO products (id, name, price, stock_quantity, stock_alert_level, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        String updateProductQuery = "UPDATE products SET stock_quantity = stock_quantity + ?, updated_at = NOW() WHERE id = ?";
        String updateWarehouseQuery = "UPDATE warehouse SET quantity = quantity - ? WHERE warehouse_id = ? AND product_id = ?";

        try (PreparedStatement checkStockStmt = connection.prepareStatement(checkStockQuery)) {
            checkStockStmt.setInt(1, warehouseId);
            checkStockStmt.setInt(2, productId);

            try (ResultSet rs = checkStockStmt.executeQuery()) {
                if (rs.next()) {
                    String itemName = rs.getString("item_name");
                    int availableStock = rs.getInt("quantity");

                    if (availableStock >= quantity) {
                        try (PreparedStatement checkProductStmt = connection.prepareStatement(checkProductQuery)) {
                            checkProductStmt.setInt(1, productId);
                            ResultSet productRs = checkProductStmt.executeQuery();

                            if (productRs.next()) {
                                try (PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery)) {
                                    updateProductStmt.setInt(1, quantity);
                                    updateProductStmt.setInt(2, productId);
                                    updateProductStmt.executeUpdate();
                                }
                            } else {
                                try (PreparedStatement insertProductStmt = connection.prepareStatement(insertProductQuery)) {
                                    insertProductStmt.setInt(1, productId);
                                    insertProductStmt.setString(2, itemName);
                                    insertProductStmt.setDouble(3, price);
                                    insertProductStmt.setInt(4, quantity);
                                    insertProductStmt.setInt(5, stockAlertLevel);
                                    insertProductStmt.executeUpdate();
                                }
                            }
                        }

                        try (PreparedStatement updateWarehouseStmt = connection.prepareStatement(updateWarehouseQuery)) {
                            updateWarehouseStmt.setInt(1, quantity);
                            updateWarehouseStmt.setInt(2, warehouseId);
                            updateWarehouseStmt.setInt(3, productId);
                            updateWarehouseStmt.executeUpdate();
                        }

                        System.out.println("✅ Stock successfully moved from warehouse to product shelf!");
                    } else {
                        System.out.println("❌ Not enough stock in warehouse for Product ID: " + productId);
                    }
                } else {
                    System.out.println("❌ No stock found in warehouse for Product ID: " + productId);
                }
            }
        }
    }

    // ✅ Get low stock items (Stock < stock_alert_level)
    public List<Map<String, Object>> getLowStockItems() throws SQLException {
        String query = "SELECT id, name, stock_quantity, stock_alert_level FROM products WHERE stock_quantity < stock_alert_level";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> lowStockList = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("Item Code", rs.getInt("id"));
                item.put("Item Name", rs.getString("name"));
                item.put("Stock Quantity", rs.getInt("stock_quantity"));
                item.put("Reorder Level", rs.getInt("stock_alert_level"));
                lowStockList.add(item);
            }

            return lowStockList;
        }
    }

    private boolean doesProductExist(int productId) throws SQLException {
        String query = "SELECT id FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addToStock(int productId, String itemName, double price, int stockQuantity,
                           int stockAlertLevel, String batchDate, String expiryDate, int warehouseId) throws SQLException {

        if (!doesProductExist(productId)) {
            System.out.println("❌ Error: Product ID " + productId + " does not exist in the products table.");
            return;
        }

        String query = "INSERT INTO stock (product_id, item_name, price, quantity, stock_alert_level, batch_date, expiry_date, warehouse_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setString(2, itemName);
            stmt.setDouble(3, price);
            stmt.setInt(4, stockQuantity);
            stmt.setInt(5, stockAlertLevel);
            stmt.setString(6, batchDate);
            stmt.setString(7, expiryDate);
            stmt.setInt(8, warehouseId);
            stmt.executeUpdate();
            System.out.println("✅ Stock successfully added!");
        }
    }

    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    private void executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement stmt = prepareStatement(query, params)) {
            stmt.executeUpdate();
        }
    }
}