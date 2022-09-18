package spl.assignment.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import spl.assignment.conf.Conf;
import spl.assignment.server.Server;

public class ServerGui {

    private final Server server;

    private final JFrame mainFrame;
    private final JPanel mainPanel;
    private final JButton stopButton;
    private final JLabel infoLabel;
    private final JTextArea logField;

    private final Thread refreshThread;

    public ServerGui(Server server) {
        this.server = server;

        mainFrame = new JFrame();
        mainFrame.setTitle("Server");
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainFrame.add(mainPanel);

        JPanel infoAndStopButton = new JPanel();
        infoAndStopButton.setLayout(new BoxLayout(infoAndStopButton, BoxLayout.Y_AXIS));

        infoLabel = new JLabel();
        // Display server.getPort()
        infoLabel.setText("Server on port " + server.getPort());
        infoAndStopButton.add(infoLabel);

        stopButton = new JButton();
        stopButton.setText("Stop");
        stopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }

        });
        infoAndStopButton.add(stopButton);
        mainPanel.add(infoAndStopButton);

        logField = new JTextArea();
        logField.setEditable(false);
        mainPanel.add(new JScrollPane(logField));

        // If logging is disabled, hide logField
        logField.setVisible(false);

        Runnable refresher = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    update();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        };

        refreshThread = new Thread(refresher);
        refreshThread.start();

        // infoLabel.setText(String.format("Port:", args));

    }

    private void update() {
        String logs = this.server.getLogger().getFullLog();
        logField.setText(logs);
    }

    public void show() {
        this.mainFrame.setVisible(true);
    }

}
