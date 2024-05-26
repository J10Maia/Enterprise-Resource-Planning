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
        int minutesPassed = 0;
        int sleepPeriods = 0;

        // Fetch the current minute value from the database
        try {
            minutesPassed = dbManager.getCurrentMinute();
            System.out.println("Starting from minute: " + minutesPassed);
        } catch (SQLException e) {
            System.out.println("Failed to fetch current minute: " + e.getMessage());
            return;
        }

        // List to store refused orders
        List<Order> refusedOrders = new ArrayList<>();

        // Continuously fetch new orders every 10 seconds
        while (true) {
            try {
                List<Order> orders = dbManager.getAllOrders(); // Fetch all orders from the database

                if (orders.isEmpty()) {
                    System.out.println("No new orders to process.");
                } else {
                    // Process each order
                    for (Order order : orders) {
                        List<Piece> allPieces = new ArrayList<>();
                        int beginDate = -1;  // Initialize with an invalid value
                        boolean orderRefused = false;

                        for (int i = 0; i < order.getQuantity(); i++) {
                            Piece piece = new Piece(order.getWorkPiece());
                            if (piece.totalTime() + minutesPassed > order.getDueDate()) {
                                orderRefused = true;
                                break;  // Skip this order
                            } else {
                                List<Piece> listP = piece.getProduction(order.getDueDate(), mm);
                                allPieces.addAll(listP);
                                // Set beginDate to the first piece's production date
                                if (beginDate == -1 && !listP.isEmpty()) {
                                    beginDate = listP.get(0).getDay();
                                }
                            }
                        }

                        if (orderRefused) {
                            refusedOrders.add(order);
                            System.out.println("Order refused: " + order.getNumber());

                            // Insert the refused order into the database
                            try {
                                dbManager.insertRefusedOrder(order);
                            } catch (SQLException e) {
                                System.out.println("Failed to insert refused order " + order.getNumber() + ": " + e.getMessage());
                            }
                            continue;  // Skip the rest of the loop for this order
                        }

                        int finishDate = order.getDueDate();  // Set finish date to order's due date

                        System.out.println("Processing order: " + order.getNumber());
                        for (Piece piece : allPieces) {
                            System.out.println(piece);
                        }
                        System.out.println(""); // For better readability between orders

                        // Insert the processed order and its transformations into the database
                        try {
                            dbManager.insertOrder(order, allPieces);
                        } catch (SQLException e) {
                            System.out.println("Failed to insert order " + order.getNumber() + ": " + e.getMessage());
                        }

                        // Insert the begin and finish dates of the order
                        try {
                            dbManager.insertDuration(order.getNumber(), beginDate, finishDate);
                        } catch (SQLException e) {
                            System.out.println("Failed to insert duration for order " + order.getNumber() + ": " + e.getMessage());
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
                sleepPeriods++;
                if (sleepPeriods == 6) {
                    minutesPassed++;
                    System.out.println("Minutes passed: " + minutesPassed);
                    sleepPeriods = 0;

                    // Update the current minute in the database
                    try {
                        dbManager.updateCurrentMinute(minutesPassed);
                    } catch (SQLException e) {
                        System.out.println("Failed to update current minute: " + e.getMessage());
                    }
                }

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
