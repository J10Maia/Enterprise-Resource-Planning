package infi2024;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize DatabaseManager and fetch orders
        List<Order> orders;
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            dbManager.setupDatabase();  // Setup the database schema and table
            orders = dbManager.getAllOrders(); // Fetch all orders from the database
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return;
        }

        // Initialize MachineManager with machines
        List<Machine> list = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            list.add(new Machine("M" + i));
            list.add(new Machine("M" + i));
            list.add(new Machine("M" + i));
        }

        MachineManager mm = new MachineManager(list);

        // Process each order
        for (Order order : orders) {
            Piece p = new Piece(order.getWorkPiece());
            List<Piece> listP = p.getProduction(order.getDueDate(), mm);

            System.out.println("Processing order: " + order.getNumber());
            for (Piece piece : listP) {
                System.out.println(piece);
            }
            System.out.println(""); // For better readability between orders

            // Insert the processed order and its transformations into the database
            try {
                dbManager.insertOrder(order, listP);
            } catch (SQLException e) {
                System.out.println("Failed to insert order " + order.getNumber() + ": " + e.getMessage());
            }
        }

        // Display all machines after processing orders
        for (Machine m : list) {
            System.out.println(m);
        }

        // Close the database connection
        try {
            if (dbManager != null) {
                dbManager.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database: " + e.getMessage());
        }
    }
}
