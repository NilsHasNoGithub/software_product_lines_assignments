package spl.assignment.main;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;

import spl.assignment.client.Client;
import spl.assignment.encryption.EncrypterDecrypter;
import spl.assignment.encryption.Reverser;
import spl.assignment.encryption.SeededEncDec;
import spl.assignment.server.Server;

public class Main {

    public static final EncrypterDecrypter ENC_DEC = new Reverser(new SeededEncDec(42));

    private static void startServer() {
        int port = Integer.parseInt(JOptionPane.showInputDialog("Enter Port Number"));

        // Create server
        final Server server = new Server(port, ENC_DEC);

        Runnable serverRunnable = new Runnable() {

            @Override
            public void run() {
                server.blockingStart();
            }

        };

        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();

        ServerGui gui = new ServerGui(server);
        server.addObserver(gui);
        
        gui.show();

    }

    private static void startClient() {
        JTextField hostField = new JTextField();
        JTextField portField = new JTextField();
        JTextField passwordField = new JTextField();
        passwordField.setText("123456789");
        JTextField usernameField = new JTextField();

        JPanel pane = new JPanel();
        // Put labels and fields in grid layout
        pane.setLayout(new GridLayout(4, 2));
        pane.add(new JLabel("Host:"));
        pane.add(hostField);
        pane.add(new JLabel("Port:"));
        pane.add(portField);
        pane.add(new JLabel("Password:"));
        pane.add(passwordField);
        pane.add(new JLabel("Username:"));
        pane.add(usernameField);

        int result = JOptionPane.showConfirmDialog(null, pane, "Enter Client Info", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            String password = passwordField.getText();
            String username = usernameField.getText();

            Client client = new Client(port, host, ENC_DEC, username, password);

            ClientGui gui = new ClientGui(client);
            gui.show();
        }

    }

    public static void main(String[] args) {
        String[] options = new String[] { "Server", "Client" };
        int chosen = JOptionPane.showOptionDialog(null, "Start application as server or client?", "Application type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);

        boolean is_server;
        if (chosen == 0) {
            is_server = true;
        } else if (chosen == 1) {
            is_server = false;
        } else {
            return;
        }

        if (is_server) {
            startServer();
        } else {
            startClient();
        }
    }
}
