package command;

import factory.ServiceFactory;
import service.SalesService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReshelvingCommand implements Command {
    private final SalesService salesService = ServiceFactory.getSalesService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        System.out.print("\nEnter the date (YYYY-MM-DD) for the reshelving report: ");
        String date = scanner.next();

        List<Map<String, Object>> reshelvingData = salesService.getReshelvingReport(date);

        if (reshelvingData.isEmpty()) {
            System.out.println("✅ No items need to be reshelved for the given date.");
            return;
        }

        System.out.println("\n=== Reshelving Report for " + date + " ===");
        System.out.println("--------------------------------------");
        for (Map<String, Object> item : reshelvingData) {
            System.out.println("Item Code: " + item.get("Item Code"));
            System.out.println("Item Name: " + item.get("Item Name"));
            System.out.println("Total Sold: " + item.get("Total Sold"));
            System.out.println("--------------------------------------");
        }
    }
}
