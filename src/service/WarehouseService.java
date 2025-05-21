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

    // Transfer stock from warehouse to product
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

    // Execute query with parameters
    private ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement stmt = prepareStatement(query, params);
        return stmt.executeQuery();
    }

    // Execute update with parameters
    private void executeUpdate(String query, Object... params) throws SQLException {
        PreparedStatement stmt = prepareStatement(query, params);
        stmt.executeUpdate();
    }

    // Prepare statement with parameters
    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }
}
