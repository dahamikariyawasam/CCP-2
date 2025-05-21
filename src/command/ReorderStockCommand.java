package command;

import factory.ServiceFactory;
import service.StockService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReorderStockCommand implements Command {
    private final StockService stockService = ServiceFactory.getStockService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        List<Map<String, Object>> lowStockItems = stockService.getLowStockItems();

        if (lowStockItems.isEmpty()) {
            System.out.println("✅ All stock levels are above the reorder level.");
            return;
        }

        System.out.println("\n=== Reorder Stock Report ===");
        for (Map<String, Object> item : lowStockItems) {
            System.out.println("--------------------------------------");
            System.out.println("Item Code: " + item.get("Item Code"));
            System.out.println("Item Name: " + item.get("Item Name"));
            System.out.println("Stock Quantity: " + item.get("Stock Quantity"));
            System.out.println("Reorder Level: " + item.get("Reorder Level"));
        }
        System.out.println("--------------------------------------");
    }
}
