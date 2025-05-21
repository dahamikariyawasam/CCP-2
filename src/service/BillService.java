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

public class BillService {
    private final Connection connection;

    public BillService(SingletonDatabase database) {
        this.connection = database.getConnection();
    }

    // ✅ Generate the next bill serial number
    public int generateSerialNumber() throws SQLException {
        String query = "SELECT COALESCE(MAX(serial_number), 0) + 1 AS next_serial FROM bills";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("next_serial");
            }
        }
        return 1; // Default to 1 if no bills exist
    }

    // ✅ Save the bill to the database
    public boolean saveBill(int serialNumber, String itemName, int quantity, double fullPrice,
                            double discount, double totalPrice, double cashTendered, double change) throws SQLException {
        String query = "INSERT INTO bills (serial_number, bill_date, item_name, quantity, full_price, discount, " +
                "total_price, cash_tendered, change_amount) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, serialNumber);
            stmt.setString(2, itemName);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, fullPrice);
            stmt.setDouble(5, discount);
            stmt.setDouble(6, totalPrice);
            stmt.setDouble(7, cashTendered);
            stmt.setDouble(8, change);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // ✅ Return true if the bill was saved successfully
        }
    }

    // ✅ Retrieve all bills from the database
    public List<Map<String, Object>> getAllBills() throws SQLException {
        String query = "SELECT serial_number, bill_date, item_name, quantity, full_price, discount, " +
                "total_price, cash_tendered, change_amount FROM bills ORDER BY bill_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            List<Map<String, Object>> billsList = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> bill = new LinkedHashMap<>();
                bill.put("Serial Number", rs.getInt("serial_number"));
                bill.put("Bill Date", rs.getTimestamp("bill_date"));
                bill.put("Item Name", rs.getString("item_name"));
                bill.put("Quantity", rs.getInt("quantity"));
                bill.put("Full Price", rs.getDouble("full_price"));
                bill.put("Discount", rs.getDouble("discount"));
                bill.put("Total Price", rs.getDouble("total_price"));
                bill.put("Cash Tendered", rs.getDouble("cash_tendered"));
                bill.put("Change Amount", rs.getDouble("change_amount"));

                billsList.add(bill);
            }
            return billsList;
        }
    }
}
