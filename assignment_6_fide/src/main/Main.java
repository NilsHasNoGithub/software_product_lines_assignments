package main;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.util.List;

import client.Client;
import encryption.EncrypterDecrypter;
import encryption.ListOfEncDecs;
import server.Server;

import java.util.ArrayList;
import java.util.List; 

public class Main {
	
	private static void startServerUi(Server server) {
		ServerCui gui = new ServerCui(server);
        gui.show();
	}

    private static void startServer() {
        int port = Integer.parseInt(JOptionPane.showInputDialog("Enter Port Number"));

        // Create server
        final Server server = new Server(port, mkEncDec());

        Runnable serverRunnable = new Runnable() {

            @Override
            public void run() {
                server.blockingStart();
            }

        };

        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();

        
        startServerUi(server);
    }
    
    private static List<EncrypterDecrypter> getSubEncDecs() {
    	return new ArrayList<EncrypterDecrypter>();
    }
    
    public static EncrypterDecrypter mkEncDec() {
    	return new ListOfEncDecs(getSubEncDecs().toArray(new EncrypterDecrypter[0]));
    }
    
    private static void startClientUi(Client client) {
    	ClientGui gui = new ClientGui(client);
        gui.show();
    }

    private static void startClient() {
        JTextField hostField = new JTextField();
        JTextField portField = new JTextField();
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

        int result = JOptionPane.showConfirmDialog(null, pane, "Enter Client Info", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            String username = usernameField.getText();

            Client client = new Client(port, host, mkEncDec(), username);

            startClientUi(client);
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
