package infi2024;

import java.sql.*;

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

    public void insertOrder(Order order) throws SQLException {
        // SQL statement for inserting data into the Orders table
        String insertSQL = "INSERT INTO infi2024.orders (" +
                           "Number, WorkPiece, Quantity, DueDate, LatePenalty, EarlyPenalty, ClientNameId" +
                           ") VALUES (?, ?, ?, ?, ?, ?, ?)" +
                           " ON CONFLICT (Number) DO NOTHING;";  // Prevent duplicate entries
    
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, order.number);
            pstmt.setString(2, order.workPiece);
            pstmt.setInt(3, order.quantity);
            pstmt.setInt(4, order.dueDate);
            pstmt.setInt(5, order.latePenalty);
            pstmt.setInt(6, order.earlyPenalty);
            pstmt.setString(7, order.clientNameId);
    
            int affectedRows = pstmt.executeUpdate();  // Execute the insert statement
    
            if (affectedRows > 0) {
                System.out.println("Order inserted successfully: " + order.number);
            } else {
                System.out.println("Order already exists and was not inserted: " + order.number);
            }
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            throw e;  // Rethrow the exception to handle it in the calling code if necessary
        }
    }
    

    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}
