package command;

import factory.ServiceFactory;
import service.BillService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BillReportCommand implements Command {
    private final BillService billService = ServiceFactory.getBillService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        List<Map<String, Object>> billData = billService.getAllBills();

        if (billData.isEmpty()) {
            System.out.println("No bill transactions found.");
            return;
        }

        System.out.println("\n=== Bill Report ===");
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-20s %-15s %-10s %-12s %-12s %-15s %-15s%n",
                "Serial Number", "Bill Date", "Item Name", "Quantity", "Full Price", "Total Price", "Cash Tendered", "Change Amount");
        System.out.println("------------------------------------------------------------------------------------------------------");

        for (Map<String, Object> bill : billData) {
            System.out.printf("%-15d %-20s %-15s %-10d Rs. %-10.2f Rs. %-10.2f Rs. %-10.2f Rs. %-10.2f%n",
                    bill.get("Serial Number"),
                    bill.get("Bill Date"),
                    bill.get("Item Name"),
                    bill.get("Quantity"),
                    bill.get("Full Price"),
                    bill.get("Total Price"),
                    bill.get("Cash Tendered"),
                    bill.get("Change Amount"));
        }

        System.out.println("------------------------------------------------------------------------------------------------------");
    }
}
