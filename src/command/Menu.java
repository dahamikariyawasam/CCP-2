package command;

import service.AuthService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final Map<Integer, Command> commands = new HashMap<>();
    private final AuthService authService = new AuthService(); //  User authentication service

    public Menu() {
        registerCommands();
    }

    private void registerCommands() {
        commands.put(1, new AddStockCommand());
        commands.put(2, new AddProductCommand());
        commands.put(3, new CheckoutCommand());
        commands.put(4, new CreateUserCommand());
        commands.put(5, new CheckStockCommand());
        commands.put(6, new AddToWarehouseCommand());
        commands.put(7, new DailySalesReportCommand());
        commands.put(8, new BillReportCommand());
        commands.put(9, new ReorderStockCommand());
        commands.put(10, new ReshelvingCommand());


    }

    public void display() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        //  Step 1: Login first
        if (!login(scanner)) {
            System.out.println("Exiting... Invalid login attempts.");
            return; // Exit if login fails
        }

        //  Step 2: Display menu after successful login
        while (true) {
            System.out.println("\n=== Product Management System ===");
           // System.out.println("1. Add Stock");
            System.out.println("2. Add Stock to Product Shelf");
            System.out.println("3. Checkout");
            System.out.println("4. Create User");
            System.out.println("5. Check Current Stock in the product shelf");
            System.out.println("6. Add Stock to Warehouse");
            System.out.println("7. Total Sales Report");
            System.out.println("8. View Bill Report");
            System.out.println("9. Reorder Stock");
            System.out.println("10. Reshelving Report");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            if (choice == 12) {
                System.out.println("Exiting... Goodbye!");
                break;
            }

            Command command = commands.get(choice);
            if (command != null) {
                command.execute(scanner);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    //  Login method
    private boolean login(Scanner scanner) throws SQLException {
        System.out.println("\n===.... SYOS ....===");
        System.out.println("\n=== Login Required ===");

        int attempts = 3; // Allow 3 attempts
        while (attempts > 0) {
            System.out.print("Enter username: ");
            String username = scanner.next();
            System.out.print("Enter password: ");
            String password = scanner.next();

            if (authService.validateUser(username, password)) {
                System.out.println("✅ Login successful! Access granted.");
                return true;
            } else {
                attempts--;
                System.out.println("❌ Invalid credentials. Attempts left: " + attempts);
            }
        }
        return false; // Login failed
    }
}
