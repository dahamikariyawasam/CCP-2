package command;

import factory.ServiceFactory;
import service.StockService;

import java.sql.SQLException;
import java.util.Scanner;

public class AddProductCommand implements Command {
    private final StockService stockService = ServiceFactory.getStockService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        System.out.print("Enter warehouse ID: ");
        int warehouseId = scanner.nextInt();

        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();

        System.out.print("Enter quantity to move: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter stock alert level: ");
        int stockAlertLevel = scanner.nextInt();

        // Move stock from warehouse to product shelf (with required price and alert level)
        stockService.moveStockToProductShelf(warehouseId, productId, quantity, price, stockAlertLevel);
    }
}