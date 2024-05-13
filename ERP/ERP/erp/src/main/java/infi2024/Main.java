package infi2024;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize DatabaseManager and fetch orders
        List<Order> orders;
        try {
            DatabaseManager dbManager = new DatabaseManager();
            orders = dbManager.getAllOrders(); // Fetch all orders from the database
            dbManager.close(); // Close database connection
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return;
        }

        // Initialize MachineManager with machines
        List<Machine> list = new ArrayList<>();
        list.add(new Machine("M1"));
        list.add(new Machine("M2"));
        list.add(new Machine("M3"));
        list.add(new Machine("M4"));
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
        }

        // Display all machines after processing orders
        for (Machine m : list) {
            System.out.println(m);
        }
    }
}
