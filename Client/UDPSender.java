import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender extends JFrame implements ActionListener {

    private JTextField ipField;
    private JTextField portField;
    private JTextArea logArea;
    private JButton sendButton;

    public UDPSender() {
        super("UDP Sender");

        // Layout components
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel ipLabel = new JLabel("IP Address:");
        ipField = new JTextField(15);
        ipField.setText("localhost");
        JLabel portLabel = new JLabel("Port:");
        portField = new JTextField(5);
        portField.setText("12345");
        inputPanel.add(ipLabel);
        inputPanel.add(ipField);
        inputPanel.add(portLabel);
        inputPanel.add(portField);

        logArea = new JTextArea(10, 60);
        logArea.setEditable(false);

        sendButton = new JButton("Send Files");
        sendButton.addActionListener(this);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(sendButton, BorderLayout.SOUTH);

        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String ip = ipField.getText();
            int port;
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                logArea.append("Invalid port number\n");
                return;
            }

            String directory = chooseDirectory();
            if (directory == null) {
                return;
            }

            logArea.setText(""); // Clear log area
            sendFiles(ip, port, directory);
        }
    }

    private String chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    private void sendFiles(String ip, int port, String directory) {
        try {
            DatagramSocket socket = new DatagramSocket();
            File dir = new File(directory);
            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    byte[] data = new byte[(int) file.length()];
                    FileInputStream inputStream = new FileInputStream(file);
                    inputStream.read(data);
                    InetAddress address = InetAddress.getByName(ip);
                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                    socket.send(packet);
                    logArea.append("Data sent for " + file.getName() + " to " + ip + ":" + port + "\n");
                    inputStream.close();
                }
            }
            socket.close();
        } catch (Exception ex) {
            logArea.append("An error occurred: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new UDPSender();
    }
}
