package command;

import factory.ServiceFactory;
import service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class CreateUserCommand implements Command {
    private final UserService userService = ServiceFactory.getUserService();

    @Override
    public void execute(Scanner scanner) throws SQLException {
        System.out.print("Enter username: ");
        String username = scanner.next();

        System.out.print("Enter password: ");
        String password = scanner.next();

        System.out.print("Enter role (e.g., admin, user): ");
        String role = scanner.next();

        boolean success = userService.createUser(username, password, role);

        if (success) {
            System.out.println("User created successfully!");
        } else {
            System.out.println("Failed to create user. Username might already exist.");
        }
    }
}
