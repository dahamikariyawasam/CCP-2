package service;

import util.SingletonDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarehouseService {
    private final Connection connection;

    public WarehouseService(SingletonDatabase db) {
        this.connection = db.getConnection();
    }

    // ✅ Add new item or update existing warehouse stock
    public void addOrUpdateWarehouseStock(int productId, String itemName, String batchDate,
                                          String expiryDate, int quantity, int warehouseId) throws SQLException {

        String checkQuery = "SELECT id FROM warehouse WHERE product_id = ? AND batch_date = ? AND expiry_date = ? AND warehouse_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, productId);
            checkStmt.setString(2, batchDate);
            checkStmt.setString(3, expiryDate);
            checkStmt.setInt(4, warehouseId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String updateQuery = "UPDATE warehouse SET quantity = quantity + ?, updated_at = NOW() WHERE id = ?";
                    executeUpdate(updateQuery, quantity, id);
                } else {
                    String insertQuery = "INSERT INTO warehouse (product_id, item_name, batch_date, expiry_date, quantity, location, warehouse_id, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, 'Unknown', ?, NOW())";
                    executeUpdate(insertQuery, productId, itemName, batchDate, expiryDate, quantity, warehouseId);
                }
            }
        }
    }

    // 🔁 Helper: Execute update with parameters
    private void executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement stmt = prepareStatement(query, params);
        stmt.executeUpdate();
    }

    // 🔁 Helper: Prepare statement
    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    // ✅ Transfer warehouse stock to product stock
    public void transferToProduct(int warehouseId, int quantity) throws SQLException {
        String fetchQuery = "SELECT product_id, quantity FROM warehouse WHERE id = ?";
        String updateWarehouseQuery = "UPDATE warehouse SET quantity = quantity - ? WHERE id = ?";
        String updateProductQuery = "UPDATE products SET stock_quantity = stock_quantity + ?, updated_at = NOW() " +
                "WHERE id = (SELECT product_id FROM warehouse WHERE id = ?)";

        ResultSet rs = executeQuery(fetchQuery, warehouseId);
        if (rs.next() && rs.getInt("quantity") >= quantity) {
            executeUpdate(updateWarehouseQuery, quantity, warehouseId);
            executeUpdate(updateProductQuery, quantity, warehouseId);
        } else {
            throw new IllegalArgumentException("Not enough stock in the warehouse.");
        }
    }

    // 🔁 Helper: Execute select query
    private ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement stmt = prepareStatement(query, params);
        return stmt.executeQuery();
    }

    // ✅ NEW: Update quantity for all records with a given product ID
    public void updateQuantityByProductId(int productId, int quantity) throws SQLException {
        String updateQuery = "UPDATE warehouse SET quantity = quantity + ?, updated_at = NOW() WHERE product_id = ?";
        executeUpdate(updateQuery, quantity, productId);
    }

    // ✅ NEW: Reduce quantity from warehouse based on product ID
    public void reduceWarehouseStock(int productId, int quantity) throws SQLException {
        String selectQuery = "SELECT quantity FROM warehouse WHERE product_id = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, productId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int available = rs.getInt("quantity");
                    if (available < quantity) {
                        throw new IllegalArgumentException("Not enough stock in warehouse.");
                    }

                    String updateQuery = "UPDATE warehouse SET quantity = quantity - ?, updated_at = NOW() WHERE product_id = ?";
                    executeUpdate(updateQuery, quantity, productId);
                } else {
                    throw new IllegalArgumentException("Product not found in warehouse.");
                }
            }
        }
    }
}
