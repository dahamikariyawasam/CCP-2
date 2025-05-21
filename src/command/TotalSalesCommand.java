package command;

import factory.ServiceFactory;
import service.SalesService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TotalSalesCommand implements Command {
    private final SalesService salesService = ServiceFactory.getSalesService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        System.out.print("\nEnter the date (YYYY-MM-DD) for the sales report: ");
        String date = scanner.next();

        List<Map<String, Object>> salesData = salesService.getTotalSalesByDate(date);

        if (salesData.isEmpty()) {
            System.out.println("No sales data found for the given date.");
            return;
        }

        System.out.println("\n=== Total Sales Report for " + date + " ===");

        // ✅ Print as a simple list (NO TABLE FORMAT)
        for (Map<String, Object> sale : salesData) {
            System.out.println("--------------------------------------");
            System.out.println("Item Code: " + sale.get("Item Code"));
            System.out.println("Item Name: " + sale.get("Item Name"));
            System.out.println("Total Quantity: " + sale.get("Total Quantity"));
            System.out.println("Total Revenue: Rs. " + sale.get("Total Revenue"));
        }
        System.out.println("--------------------------------------");
    }
}
