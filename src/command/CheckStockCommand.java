package command;

import factory.ServiceFactory;
import service.InventoryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class CheckStockCommand implements Command {
    private final InventoryService inventoryService;

    public CheckStockCommand() {
        this.inventoryService = ServiceFactory.getInventoryService();
    }

    @Override
    public void execute(Scanner scanner) throws SQLException {
        displayCurrentStock();
    }

    private void displayCurrentStock() throws SQLException {
        List<Map<String, Object>> stockData = inventoryService.getCurrentStock();

        if (stockData == null || stockData.isEmpty()) {
            System.out.println("\nNo stock available.");
            return;
        }

        printStockHeader();
        stockData.forEach(this::printStockDetails);
    }

    private void printStockHeader() {
        System.out.println("\n--- Current Stock ---");
        System.out.printf("%-12s %-20s %-15s\n", "Product ID", "Product Name", "Stock Quantity");
        System.out.println("---------------------------------------------------");
    }

    private void printStockDetails(Map<String, Object> stock) {
        System.out.printf(
                "%-12d %-20s %-15d\n",
                stock.get("id"),
                stock.get("name"),
                stock.get("stock_quantity")
        );
    }
}
