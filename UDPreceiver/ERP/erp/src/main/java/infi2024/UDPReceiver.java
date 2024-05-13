package infi2024;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

public class UDPReceiver {
    private int port;
    private DatabaseManager dbManager;

    public UDPReceiver(int port, DatabaseManager dbManager) {
        this.port = port;
        this.dbManager = dbManager;
    }

    public void start() throws Exception {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[65535];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String receivedData = new String(packet.getData(), 0, packet.getLength());
                List<Order> orders = XMLParser.parseXML(receivedData);
                for (Order order : orders) {
                    dbManager.insertOrder(order);
                }
            }
        }
    }
}
