package spl.assignment.main;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;

import spl.assignment.authentication.Authenticator;
import spl.assignment.authentication.NoAuth;
import spl.assignment.client.Client;
import spl.assignment.color.ChatColorComponent;
import spl.assignment.color.ColorComponent;
import spl.assignment.encryption.EncrypterDecrypter;
import spl.assignment.encryption.ListOfEncDecs;
import spl.assignment.encryption.Reverser;
import spl.assignment.encryption.SeededEncDec;
import spl.assignment.server.Server;

public class Main {
    public static String PASSWORD = "123456789";


    /// Configuration for message encryption and decryption (Can be any list of encrypters, empty list for no encryption/decryption)
    public static final EncrypterDecrypter ENC_DEC = new ListOfEncDecs(
            new EncrypterDecrypter[] { new Reverser(), new SeededEncDec(42) });

    /// Configuration of authentication (Can also be NoAuth)
    // public static final Authenticator AUTHENTICATOR = new PlainPasswordAuth(PASSWORD);
    public static final Authenticator AUTHENTICATOR = new NoAuth();

    /// Configuration of chat colors
    // public static final ChatColorComponent CHAT_COLOR_COMPONENT = new NoColorComponent();
    public static final ChatColorComponent CHAT_COLOR_COMPONENT = new ColorComponent();

    /// Configuration function for creating client ui
    private static ClientUi mkClientUi(Client client) {
        // return new ClientCui(client);
        return new ClientGui(client, CHAT_COLOR_COMPONENT);
    }

    /// Configuration function for creating server ui
    private static ServerUi mkServerUi(Server server) {
        // return new ServerCui(server);
        return new ServerGui(server);
    }

    private static void startServer() {
        int port = Integer.parseInt(JOptionPane.showInputDialog("Enter Port Number"));

        // Create server
        final Server server;
        server = new Server(port, ENC_DEC, AUTHENTICATOR);
        

        Runnable serverRunnable = new Runnable() {

            @Override
            public void run() {
                server.blockingStart();
            }

        };

        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();

        ServerUi ui = mkServerUi(server);
        ui.show();
    }

    private static void startClient() {
        JTextField hostField = new JTextField();
        JTextField portField = new JTextField();
        JTextField passwordField = new JTextField();
        passwordField.setText(PASSWORD);
        JTextField usernameField = new JTextField();

        JPanel pane = new JPanel();
        // Put labels and fields in grid layout
        pane.setLayout(new GridLayout(4, 2));
        pane.add(new JLabel("Host:"));
        pane.add(hostField);
        pane.add(new JLabel("Port:"));
        pane.add(portField);

        pane.add(new JLabel("Username:"));
        pane.add(usernameField);

        if (AUTHENTICATOR.passwordRequired()) {
            pane.add(new JLabel("Password:"));
            pane.add(passwordField);
        }

        int result = JOptionPane.showConfirmDialog(null, pane, "Enter Client Info", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            String password = passwordField.getText();
            String username = usernameField.getText();

            Client client = new Client(port, host, ENC_DEC, username, AUTHENTICATOR.newInstance(password));

            ClientUi gui = mkClientUi(client);
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
