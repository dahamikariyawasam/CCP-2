package service;

import util.SingletonDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class InventoryService {
    private final Connection connection;

    public InventoryService(SingletonDatabase db) {
        this.connection = db.getConnection();
    }

    // Add new stock to the stock table
    public void addStock(int productId, String itemName, String batchDate, String expiryDate, int quantity, int warehouseId) throws SQLException {
        String query = "INSERT INTO stock (product_id, item_name, batch_date, expiry_date, quantity, warehouse_id, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, productId);
        stmt.setString(2, itemName);
        stmt.setString(3, batchDate);
        stmt.setString(4, expiryDate);
        stmt.setInt(5, quantity);
        stmt.setInt(6, warehouseId);
        stmt.executeUpdate();
    }

    // Add stock to the warehouse and update stock table
    public void addStockToWarehouse(int productId, String itemName, String batchDate, String expiryDate, int quantity, int warehouseId) throws SQLException {
        // Update warehouse table
        String warehouseQuery = "INSERT INTO warehouse (product_id, item_name, batch_date, expiry_date, quantity, warehouse_id, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        PreparedStatement warehouseStmt = connection.prepareStatement(warehouseQuery);
        warehouseStmt.setInt(1, productId);
        warehouseStmt.setString(2, itemName);
        warehouseStmt.setString(3, batchDate);
        warehouseStmt.setString(4, expiryDate);
        warehouseStmt.setInt(5, quantity);
        warehouseStmt.setInt(6, warehouseId);
        warehouseStmt.executeUpdate();

        // Deduct stock from stock table
        String stockUpdateQuery = "UPDATE stock SET quantity = quantity - ? WHERE product_id = ? AND item_name = ? AND batch_date = ? AND expiry_date = ?";
        PreparedStatement stockStmt = connection.prepareStatement(stockUpdateQuery);
        stockStmt.setInt(1, quantity);
        stockStmt.setInt(2, productId);
        stockStmt.setString(3, itemName);
        stockStmt.setString(4, batchDate);
        stockStmt.setString(5, expiryDate);
        stockStmt.executeUpdate();
    }

    // Checkout product with discount
    public boolean checkoutProduct(int productId, int quantity) throws SQLException {
        if (!checkStockAvailability(productId, quantity)) {
            return false; // Insufficient stock
        }

        // Get product price and discount
        double price = getProductPrice(productId);
        double discount = getProductDiscount(productId);

        // Calculate total price after discount
        double totalPrice = (price * quantity) * (1 - discount / 100);

        // Deduct stock from the product table
        String updateStockQuery = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ?";
        PreparedStatement updateStockStmt = connection.prepareStatement(updateStockQuery);
        updateStockStmt.setInt(1, quantity);
        updateStockStmt.setInt(2, productId);
        updateStockStmt.executeUpdate();

        System.out.println("Checkout successful. Total price after discount: Rs. " + totalPrice);
        return true;
    }

    // Check if the required quantity of a product is available in stock
    public boolean checkStockAvailability(int productId, int requiredQuantity) throws SQLException {
        String query = "SELECT stock_quantity FROM products WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int stockQuantity = rs.getInt("stock_quantity");
            return stockQuantity >= requiredQuantity;
        }
        return false; // Product not found or insufficient stock
    }

    // Get product discount
    public double getProductDiscount(int productId) throws SQLException {
        String query = "SELECT discount FROM products WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getDouble("discount");
        }
        return 0.0; // No discount found
    }
    public void transferStockToProduct(int warehouseId, int quantity) throws SQLException {
        String fetchWarehouseQuery = "SELECT product_id, quantity FROM warehouse WHERE id = ?";
        String updateWarehouseQuery = "UPDATE warehouse SET quantity = quantity - ? WHERE id = ?";
        String updateProductQuery = "UPDATE products SET stock_quantity = stock_quantity + ?, updated_at = NOW() WHERE id = ?";

        PreparedStatement fetchStmt = connection.prepareStatement(fetchWarehouseQuery);
        fetchStmt.setInt(1, warehouseId);
        ResultSet rs = fetchStmt.executeQuery();

        if (rs.next()) {
            int productId = rs.getInt("product_id");
            int availableQuantity = rs.getInt("quantity");

            if (availableQuantity < quantity) {
                throw new IllegalArgumentException("Not enough stock in the warehouse to transfer to the product.");
            }

            // Deduct quantity from the warehouse
            PreparedStatement updateWarehouseStmt = connection.prepareStatement(updateWarehouseQuery);
            updateWarehouseStmt.setInt(1, quantity);
            updateWarehouseStmt.setInt(2, warehouseId);
            updateWarehouseStmt.executeUpdate();

            // Add quantity to the product
            PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery);
            updateProductStmt.setInt(1, quantity);
            updateProductStmt.setInt(2, productId);
            updateProductStmt.executeUpdate();
        } else {
            throw new IllegalArgumentException("Warehouse stock ID not found.");
        }
    }


    // Get product price
    public double getProductPrice(int productId) throws SQLException {
        String query = "SELECT price FROM products WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getDouble("price");
        }
        return 0.0; // No price found
    }








    public String getProductName(int productId) throws SQLException {
        String query = "SELECT name FROM products WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, productId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("name");
        }
        return null; // Product not found
    }



    public List<Map<String, Object>> getCurrentStock() throws SQLException {
        final String query = "SELECT id, name, stock_quantity FROM products";
        final List<Map<String, Object>> stockList = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            stockList.add(mapStock(resultSet));
        }
        return stockList;
    }

    private Map<String, Object> mapStock(ResultSet resultSet) throws SQLException {
        Map<String, Object> stock = new HashMap<>();
        stock.put("id", resultSet.getInt("id"));
        stock.put("name", resultSet.getString("name"));
        stock.put("stock_quantity", resultSet.getInt("stock_quantity"));
        return stock;
    }




}