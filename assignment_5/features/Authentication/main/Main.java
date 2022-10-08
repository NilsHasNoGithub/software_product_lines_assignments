package main;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.Client;

/**
 * TODO description
 */
public class Main {
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

            Client client = new Client(port, host, mkEncDec(), username, password);

            ClientGui gui = new ClientGui(client);
            gui.show();
        }

    }
}