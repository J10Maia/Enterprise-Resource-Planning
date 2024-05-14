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
    // ID | Day | Peca Inicial | Peca Final
    public void setupDatabase() throws SQLException {
        String createSchemaSQL = "CREATE SCHEMA IF NOT EXISTS infi2024";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.planningtable ("
                              + "id SERIAL PRIMARY KEY, "
                              + "day INT NOT NULL, "
                              + "peca_inicial VARCHAR(255) NOT NULL, "
                              + "peca_final VARCHAR(255) NOT NULL, "
                              + "CONSTRAINT unique_id UNIQUE (id))";
    
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSchemaSQL);  // Create the schema if it doesn't exist
            stmt.execute(createTableSQL);   // Create the table if it doesn't exist
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
    /*
    public void insertOrder(Plan plan) throws SQLException {
        // SQL statement for inserting data into the planningTable
        String insertSQL = "INSERT INTO infi2024.planningTable (" +
                           "id, day, peca_inicial, peca_final" +
                           ") VALUES (?, ?, ?, ?)" +
                           " ON CONFLICT (id) DO NOTHING;";  // Prevent duplicate entries

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, plan.getId());
            pstmt.setInt(2, plan.getDay());
            pstmt.setString(3, plan.getPecaInicial());
            pstmt.setString(4, plan.getPecaFinal());

            int affectedRows = pstmt.executeUpdate();  // Execute the insert statement

            if (affectedRows > 0) {
                System.out.println("Order inserted successfully: " + plan.getId());
            } else {
                System.out.println("Order already exists and was not inserted: " + plan.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            throw e;  // Rethrow the exception to handle it in the calling code if necessary
        }
    }
    */




    public void insertOrder(Order order, List<Piece> pieces) throws SQLException {
        // SQL statement for inserting data into the planningTable without id
        String insertSQL = "INSERT INTO infi2024.planningtable (" +
                           "day, peca_inicial, peca_final" +
                           ") VALUES (?, ?, ?)";  
    
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (Piece piece : pieces) {
                pstmt.setInt(1, piece.getDay());
                String pecaInicial = piece.getPreviousPiece() != null ? piece.getPreviousPiece().getWorkPiece() : "InitialPiece"; // Handle null
                pstmt.setString(2, pecaInicial);
                pstmt.setString(3, piece.getWorkPiece());
    
                int affectedRows = pstmt.executeUpdate();  // Execute the insert statement
    
                if (affectedRows > 0) {
                    System.out.println("Order piece inserted successfully for order: " + order.getNumber());
                } else {
                    System.out.println("Order piece already exists and was not inserted for order: " + order.getNumber());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting order pieces: " + e.getMessage());
            throw e;  // Rethrow the exception to handle it in the calling code if necessary
        }
    }
    

    
    // Close the connection (assuming this method is part of DatabaseManager)
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
