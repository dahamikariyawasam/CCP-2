package command;

import factory.ServiceFactory;
import service.StockService;
import util.SingletonDatabase;

import java.sql.SQLException;
import java.util.Scanner;

public class AddStockCommand implements Command {
    private final StockService stockService = ServiceFactory.getStockService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        scanner.nextLine(); // ✅ Consume leftover newline

        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine().trim();

        if (itemName.isEmpty()) {
            System.out.println("❌ Invalid item name. Please enter a non-empty name.");
            return;
        }

        System.out.print("Enter product ID: ");
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Enter a valid product ID (integer): ");
            scanner.next(); // ✅ Consume incorrect input
        }
        int productId = scanner.nextInt();
        scanner.nextLine(); // ✅ Consume newline

        System.out.print("Enter batch date (YYYY-MM-DD): ");
        String batchDate = scanner.nextLine().trim();

        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine().trim();

        System.out.print("Enter quantity: ");
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Enter a valid quantity (integer): ");
            scanner.next();
        }
        int stockQuantity = scanner.nextInt();
        scanner.nextLine(); // ✅ Consume newline

        System.out.print("Enter warehouse ID: ");
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Enter a valid warehouse ID (integer): ");
            scanner.next();
        }
        int warehouseId = scanner.nextInt();
        scanner.nextLine(); // ✅ Consume newline

        System.out.print("Enter price: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("❌ Invalid input! Enter a valid price (decimal): ");
            scanner.next();
        }
        double price = scanner.nextDouble();
        scanner.nextLine(); // ✅ Consume newline

        System.out.print("Enter stock alert level: ");
        while (!scanner.hasNextInt()) {
            System.out.println("❌ Invalid input! Enter a valid alert level (integer): ");
            scanner.next();
        }
        int stockAlertLevel = scanner.nextInt();
        scanner.nextLine(); // ✅ Consume newline

        // ✅ Call stockService to add stock
        stockService.addToStock(productId, itemName, price, stockQuantity, stockAlertLevel, batchDate, expiryDate, warehouseId);

        // ✅ Commit transaction to save the stock
        SingletonDatabase.getInstance().getConnection().commit();

        System.out.println("✅ Stock successfully added to warehouse!");
    }
}
