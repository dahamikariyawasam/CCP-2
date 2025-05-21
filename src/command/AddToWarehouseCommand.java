package command;

import factory.ServiceFactory;
import service.StockService;

import java.sql.SQLException;
import java.util.Scanner;

public class AddToWarehouseCommand implements Command {
    private final StockService stockService = ServiceFactory.getStockService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Item Name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter Batch Date (YYYY-MM-DD): ");
        String batchDate = scanner.nextLine();

        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine();

        System.out.print("Enter Quantity to Add: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter Warehouse ID: ");
        int warehouseId = scanner.nextInt();

        boolean success = stockService.addOrUpdateWarehouseStock(
                productId, itemName, batchDate, expiryDate, quantity, warehouseId
        );

        if (success) {
            System.out.println("✅ Warehouse stock successfully added or updated!");
        } else {
            System.out.println("❌ Failed to update warehouse stock.");
        }
    }
}
