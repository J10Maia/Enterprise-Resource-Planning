package infi2024;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            dbManager.setupDatabase();
            UDPReceiver udpReceiver = new UDPReceiver(12345, dbManager);
            udpReceiver.start();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
