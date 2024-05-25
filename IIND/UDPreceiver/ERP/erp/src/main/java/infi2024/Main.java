package infi2024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main {
    private static UDPReceiver udpReceiver;
    private static JTextArea textArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UDP Order Receiver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        panel.add(controlPanel, BorderLayout.SOUTH);

        JLabel userLabel = new JLabel("Click the button to start receiving orders:");
        controlPanel.add(userLabel);

        JButton startButton = new JButton("Start");
        controlPanel.add(startButton);

        // Redirect console output to the text area
        redirectSystemStreams();

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            System.out.println("Starting the UDP Receiver...");
                            DatabaseManager dbManager = new DatabaseManager();
                            dbManager.setupDatabase();
                            udpReceiver = new UDPReceiver(12345, dbManager);
                            udpReceiver.start();
                            System.out.println("UDP Receiver started.");
                            JOptionPane.showMessageDialog(panel, "UDP Receiver started.");
                        } catch (Exception ex) {
                            System.err.println("Error starting UDP Receiver: " + ex.getMessage());
                            JOptionPane.showMessageDialog(panel, "Error starting UDP Receiver: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        frame.setVisible(true);
    }

    private static void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                textArea.append(text);
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
    }

    private static void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws java.io.IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}
