package infi2024;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize DatabaseManager
        DatabaseManager dbManager = null;
        try {
            dbManager = new DatabaseManager();
            dbManager.setupDatabase();  // Setup the database schema and table
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return;
        }

        // Initialize MachineManager with machines from the database
        List<Machine> machines = new ArrayList<>();
        try {
            machines = dbManager.getAllMachines();
        } catch (SQLException e) {
            System.out.println("Error fetching machines: " + e.getMessage());
            return;
        }

        MachineManager mm = new MachineManager(machines);

        // Continuously fetch new orders every 10 seconds
        while (true) {
            try {
                List<Order> orders = dbManager.getAllOrders(); // Fetch all orders from the database

                if (orders.isEmpty()) {
                    System.out.println("No new orders to process.");
                } else {
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

                        // Update occupied days for each machine
                        for (Machine machine : machines) {
                            try {
                                dbManager.updateMachineOccupiedDays(machine);
                            } catch (SQLException e) {
                                System.out.println("Failed to update occupied days for machine " + machine.getId() + ": " + e.getMessage());
                            }
                        }
                    }

                    // Display all machines after processing orders
                    for (Machine m : machines) {
                        System.out.println(m);
                    }
                }

                // Sleep for 10 seconds before fetching new orders again
                Thread.sleep(10000);

            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
                break;
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                break;
            }
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
