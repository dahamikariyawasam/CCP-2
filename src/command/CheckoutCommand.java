package command;

import factory.ServiceFactory;
import service.BillService;
import service.InventoryService;
import service.SalesService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CheckoutCommand implements Command {
    private final InventoryService inventoryService = ServiceFactory.getInventoryService();
    private final BillService billService = ServiceFactory.getBillService();
    private final SalesService salesService = ServiceFactory.getSalesService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        int productId = promptForProductId(scanner);
        int quantity = promptForQuantity(scanner);

        if (!validateProduct(productId) || !validateQuantity(quantity)) {
            return;
        }

        if (!inventoryService.checkStockAvailability(productId, quantity)) {
            System.out.println("Not enough quantity available for product ID: " + productId);
            return;
        }

        double fullPrice = calculateFullPrice(productId, quantity);
        double discount = inventoryService.getProductDiscount(productId);
        double discountAmount = calculateDiscount(fullPrice, discount);
        double totalPrice = calculateTotalPrice(fullPrice, discountAmount);

        String billDate = getCurrentDate();
        displayBillDetails(productId, quantity, fullPrice, discount, discountAmount, totalPrice, billDate);

        double cashTendered = promptForCashTendered(scanner, totalPrice);
        double change = calculateChange(cashTendered, totalPrice);

        System.out.print("Enter payment type (e.g., Cash, Card): ");
        String paymentType = scanner.next();

        System.out.print("Enter payment status (e.g., Completed, Pending): ");
        String paymentStatus = scanner.next();

        int userId = getCurrentUserId(); // Replace this with your logic to fetch the user ID.

        if (processCheckout(productId, userId, quantity, fullPrice, discount, totalPrice, cashTendered, change, billDate, paymentType, paymentStatus)) {
            System.out.println("Checkout completed successfully!");
        } else {
            System.out.println("Checkout failed. Please try again.");
        }
    }

    private int promptForProductId(Scanner scanner) {
        System.out.print("Enter product ID: ");
        return scanner.nextInt();
    }

    private int promptForQuantity(Scanner scanner) {
        System.out.print("Enter quantity to checkout: ");
        return scanner.nextInt();
    }

    private boolean validateProduct(int productId) throws SQLException {
        if (productId <= 0 || inventoryService.getProductName(productId) == null) {
            System.out.println("Invalid product ID.");
            return false;
        }
        return true;
    }

    private boolean validateQuantity(int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return false;
        }
        return true;
    }

    private double calculateFullPrice(int productId, int quantity) throws SQLException {
        return inventoryService.getProductPrice(productId) * quantity;
    }

    private double calculateDiscount(double fullPrice, double discount) {
        return (discount / 100) * fullPrice;
    }

    private double calculateTotalPrice(double fullPrice, double discountAmount) {
        return fullPrice - discountAmount;
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private void displayBillDetails(int productId, int quantity, double fullPrice, double discount, double discountAmount, double totalPrice, String billDate) throws SQLException {
        System.out.println("\n--- Bill Details ---");
        System.out.println("Date: " + billDate);
        System.out.println("Product: " + inventoryService.getProductName(productId));
        System.out.println("Quantity: " + quantity);
        System.out.println("Full Price: Rs." + fullPrice);
        System.out.println("Discount: " + discount + "% (-Rs." + discountAmount + ")");
        System.out.println("Total Price: Rs." + totalPrice);
    }

    private double promptForCashTendered(Scanner scanner, double totalPrice) {
        System.out.print("Enter cash tendered: ");
        double cashTendered = scanner.nextDouble();
        if (cashTendered < totalPrice) {
            System.out.println("Insufficient cash. Transaction aborted.");
            System.exit(0);
        }
        return cashTendered;
    }

    private double calculateChange(double cashTendered, double totalPrice) {
        return cashTendered - totalPrice;
    }

    private boolean processCheckout(int productId, int userId, int quantity, double fullPrice, double discount, double totalPrice, double cashTendered, double change, String billDate, String paymentType, String paymentStatus) throws SQLException {
        if (inventoryService.checkoutProduct(productId, quantity)) {
            int serialNumber = billService.generateSerialNumber();
            billService.saveBill(serialNumber, inventoryService.getProductName(productId), quantity, fullPrice, discount, totalPrice, cashTendered, change);

            // Record the sale in the sales table
            salesService.recordSale(productId, userId, quantity, totalPrice, paymentType, paymentStatus);

            displayFinalBill(serialNumber, productId, quantity, fullPrice, discount, totalPrice, cashTendered, change, billDate);
            return true;
        }
        return false;
    }

    private void displayFinalBill(int serialNumber, int productId, int quantity, double fullPrice, double discount, double totalPrice, double cashTendered, double change, String billDate) throws SQLException {
        System.out.println("\n--- Final Bill ---");
        System.out.println("Date: " + billDate);
        System.out.println("Serial Number: " + serialNumber);
        System.out.println("Product: " + inventoryService.getProductName(productId));
        System.out.println("Quantity: " + quantity);
        System.out.println("Full Price: Rs." + fullPrice);
        System.out.println("Discount: " + discount + "% (-Rs." + (fullPrice - totalPrice) + ")");
        System.out.println("Total Price: Rs." + totalPrice);
        System.out.println("Cash Tendered: Rs." + cashTendered);
        System.out.println("Change: Rs." + change);
    }

    private int getCurrentUserId() {
        // Replace this with logic to get the logged-in user's ID
        return 1; // Placeholder for demonstration purposes
    }
}
