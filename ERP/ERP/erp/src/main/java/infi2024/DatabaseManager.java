package infi2024;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection conn;

    public DatabaseManager() throws SQLException {
        String url = "jdbc:postgresql://db.fe.up.pt/infind202404";
        String user = "infind202404";
        String password = "RJiaKIGUvR";
        this.conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the PostgreSQL server successfully.");
    }

    public void setupDatabase() throws SQLException {
        String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS infi2024";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.orders ("  // Ensure correct syntax here
                              + "Number VARCHAR(255) PRIMARY KEY, "
                              + "WorkPiece VARCHAR(255), "
                              + "Quantity INT, "
                              + "DueDate INT, "
                              + "LatePenalty INT, "
                              + "EarlyPenalty INT, "
                              + "ClientNameId VARCHAR(255))";
    
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSchemaSQL);  // Create the schema if not exists
            stmt.execute(createTableSQL);   // Create the table if not exists
            System.out.println("Database schema and table setup complete.");
        } catch (SQLException e) {
            System.out.println("Error setting up database: " + e.getMessage());
            throw e;
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT Number, WorkPiece, Quantity, DueDate, LatePenalty, EarlyPenalty, ClientNameId FROM infi2024.orders";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String number = rs.getString("Number");
                String workPiece = rs.getString("WorkPiece");
                int quantity = rs.getInt("Quantity");
                int dueDate = rs.getInt("DueDate");
                int latePenalty = rs.getInt("LatePenalty");
                int earlyPenalty = rs.getInt("EarlyPenalty");
                String clientNameId = rs.getString("ClientNameId");

                orders.add(new Order(number, workPiece, quantity, dueDate, latePenalty, earlyPenalty, clientNameId));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving orders: " + e.getMessage());
            throw e;
        }
        return orders;
    }
    

    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}
