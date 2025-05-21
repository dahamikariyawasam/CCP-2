package command;

import java.sql.SQLException;
import java.util.Scanner;

public interface Command {
    void execute(Scanner scanner) throws SQLException;
}
