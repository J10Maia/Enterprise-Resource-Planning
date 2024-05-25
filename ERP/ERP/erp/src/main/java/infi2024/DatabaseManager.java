package infi2024;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
        String createPlanningTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.planningtable ("
                                      + "id SERIAL PRIMARY KEY, "
                                      + "day INT NOT NULL, "
                                      + "peca_inicial VARCHAR(255) NOT NULL, "
                                      + "peca_final VARCHAR(255) NOT NULL, "
                                      + "CONSTRAINT unique_id UNIQUE (id))";

        String createPlannedOrdersTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.planned_orders ("
                                            + "order_number VARCHAR(255) PRIMARY KEY)";

        String createMachineTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.machine ("
                                      + "machine_id SERIAL PRIMARY KEY, "
                                      + "machine_type VARCHAR(255) NOT NULL, "
                                      + "tools VARCHAR(255) NOT NULL, "
                                      + "occupied_days VARCHAR(255))"; // Store occupied days as a comma-separated string

        String createCurrentMinuteTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.current_minute ("
                                            + "minute INT PRIMARY KEY)";

        String createDurationTableSQL = "CREATE TABLE IF NOT EXISTS infi2024.duration ("
                                       + "order_number VARCHAR(255) PRIMARY KEY, "
                                       + "begin_date INT NOT NULL, "
                                       + "finish_date INT NOT NULL)";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSchemaSQL);  // Create the schema if it doesn't exist
            stmt.execute(createPlanningTableSQL);   // Create the planningtable if it doesn't exist
            stmt.execute(createPlannedOrdersTableSQL); // Create the planned_orders table
            stmt.execute(createMachineTableSQL); // Create the machine table
            stmt.execute(createCurrentMinuteTableSQL); // Create the current_minute table
            stmt.execute(createDurationTableSQL); // Create the duration table
            System.out.println("Database schema and tables setup complete.");

            // Insert initial machine data with multiple instances
            String insertMachinesSQL = "INSERT INTO infi2024.machine (machine_type, tools, occupied_days) VALUES "
                                     + "('M1', 'T1,T2,T3', ''), ('M1', 'T1,T2,T3', ''), ('M1', 'T1,T2,T3', ''),"
                                     + "('M2', 'T1,T2,T3', ''), ('M2', 'T1,T2,T3', ''), ('M2', 'T1,T2,T3', ''),"
                                     + "('M3', 'T1,T4,T5', ''), ('M3', 'T1,T4,T5', ''), ('M3', 'T1,T4,T5', ''),"
                                     + "('M4', 'T1,T4,T6', ''), ('M4', 'T1,T4,T6', ''), ('M4', 'T1,T4,T6', '');";

            stmt.execute(insertMachinesSQL);
            System.out.println("Initial machine data inserted.");

            // Insert default value into current_minute table if not exists
            String insertDefaultCurrentMinuteSQL = "INSERT INTO infi2024.current_minute (minute) VALUES (0) "
                                                 + "ON CONFLICT (minute) DO NOTHING";

            stmt.execute(insertDefaultCurrentMinuteSQL);
            System.out.println("Default current minute value inserted.");
        } catch (SQLException e) {
            System.out.println("Error setting up database: " + e.getMessage());
            throw e;
        }
    }

    //Fetch all orders from the database
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.Number, o.WorkPiece, o.Quantity, o.DueDate, o.LatePenalty, o.EarlyPenalty, o.ClientNameId "
                     + "FROM infi2024.orders o "
                     + "LEFT JOIN infi2024.planned_orders po ON o.Number = po.order_number "
                     + "WHERE po.order_number IS NULL";
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
            System.out.println("Error fetching orders: " + e.getMessage());
            throw e;
        }
        
        if (orders.isEmpty()) {
            System.out.println("No new orders.");
        }
        
        return orders;
    }
    //Insert order planning into the database
    public void insertOrder(Order order, List<Piece> pieces) throws SQLException {
        String insertSQL = "INSERT INTO infi2024.planningtable (day, peca_inicial, peca_final) VALUES (?, ?, ?)";
        String insertOrderNumberSQL = "INSERT INTO infi2024.planned_orders (order_number) VALUES (?)";
    
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL);
             PreparedStatement pstmtOrderNumber = conn.prepareStatement(insertOrderNumberSQL)) {
            
            for (Piece piece : pieces) {
                pstmt.setInt(1, piece.getDay());
                String pecaInicial = piece.getPreviousPiece() != null ? piece.getPreviousPiece().getWorkPiece() : "InitialPiece";
                pstmt.setString(2, pecaInicial);
                pstmt.setString(3, piece.getWorkPiece());
    
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Order piece inserted successfully for order: " + order.getNumber());
                } else {
                    System.out.println("Order piece already exists and was not inserted for order: " + order.getNumber());
                }
            }
            
            // Insert the order number into planned_orders
            pstmtOrderNumber.setString(1, order.getNumber());
            int affectedRowsOrder = pstmtOrderNumber.executeUpdate();
            if (affectedRowsOrder > 0) {
                System.out.println("Order number inserted into planned_orders successfully for order: " + order.getNumber());
            } else {
                System.out.println("Order number already exists in planned_orders and was not inserted for order: " + order.getNumber());
            }
        } catch (SQLException e) {
            System.out.println("Error inserting order pieces: " + e.getMessage());
            throw e;
        }
    }    
    // Fetch all machines from the database
    public List<Machine> getAllMachines() throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String query = "SELECT machine_id, machine_type, tools, occupied_days FROM infi2024.machine";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("machine_id");
                String type = rs.getString("machine_type");
                String toolsStr = rs.getString("tools");
                String occupiedDaysStr = rs.getString("occupied_days");
                List<String> tools = Arrays.asList(toolsStr.split(","));
                List<Integer> occupiedDays = new ArrayList<>();
                if (occupiedDaysStr != null && !occupiedDaysStr.isEmpty()) {
                    for (String day : occupiedDaysStr.split(",")) {
                        occupiedDays.add(Integer.parseInt(day));
                    }
                }
                machines.add(new Machine(id, type, tools, occupiedDays));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching machines: " + e.getMessage());
            throw e;
        }
        return machines;
    }
    // Update the occupied days for a machine in the database
    public void updateMachineOccupiedDays(Machine machine) throws SQLException {
        String updateSQL = "UPDATE infi2024.machine SET occupied_days = ? WHERE machine_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            StringBuilder occupiedDaysStr = new StringBuilder();
            for (int day : machine.getOccupiedDays()) {
                if (occupiedDaysStr.length() > 0) {
                    occupiedDaysStr.append(",");
                }
                occupiedDaysStr.append(day);
            }
            pstmt.setString(1, occupiedDaysStr.toString());
            pstmt.setInt(2, machine.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating machine occupied days: " + e.getMessage());
            throw e;
        }
    }

    public void updateCurrentMinute(int minute) throws SQLException {
        String sql = "INSERT INTO infi2024.current_minute (minute) VALUES (?) ON CONFLICT (minute) DO UPDATE SET minute = excluded.minute";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, minute);
            pstmt.executeUpdate();
        }
    }

    public int getCurrentMinute() throws SQLException {
        String sql = "SELECT minute FROM infi2024.current_minute ORDER BY minute DESC LIMIT 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("minute");
            } else {
                throw new SQLException("Failed to fetch the current minute from the database.");
            }
        }
    }
    
    public void insertDuration(String orderNumber, int beginDate, int finishDate) throws SQLException {
        String sql = "INSERT INTO infi2024.duration (order_number, begin_date, finish_date) VALUES (?, ?, ?) "
                   + "ON CONFLICT (order_number) DO UPDATE SET begin_date = excluded.begin_date, finish_date = excluded.finish_date";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderNumber);
            pstmt.setInt(2, beginDate);
            pstmt.setInt(3, finishDate);
            pstmt.executeUpdate();
        }
    }
    
    // Close the connection (assuming this method is part of DatabaseManager)
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
