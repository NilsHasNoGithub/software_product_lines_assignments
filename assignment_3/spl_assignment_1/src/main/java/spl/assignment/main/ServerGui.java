package spl.assignment.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import spl.assignment.server.Server;

public class ServerGui {

    private final Server server;

    private final JFrame mainFrame;
    private final JPanel mainPanel;
    private final JButton stopButton;
    private final JLabel infoLabel;
    private final JComponent logField;
    
    //#if LOGGING
//@    private final Thread refreshThread;
    //#endif

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
        
        //#if LOGGING
//@        logField = new JTextArea();
//@        ((JTextArea) logField).setEditable(false);
//@        mainPanel.add(new JScrollPane(logField));
        //#else
        logField = new JLabel("logging disabled");
        mainPanel.add(logField);
        //#endif

        //#if LOGGING
//@        Runnable refresher = new Runnable() {
//@
//@            @Override
//@            public void run() {
//@                while(true) {
//@                    update();
//@                    try {
//@                        Thread.sleep(500);
//@                    } catch (InterruptedException e) {
//@                        e.printStackTrace();
//@                    }
//@                }
//@                
//@            }
//@        
//@        };
//@
//@        refreshThread = new Thread(refresher);
//@        refreshThread.start();
        //#endif
        
        // infoLabel.setText(String.format("Port:", args));

    }
    
    //#if LOGGING
//@    private void update() {
//@        String logs = this.server.getLogger().getFullLog();
//@        logField.setText(logs);
//@    }
    //#endif


    public void show() {
        this.mainFrame.setVisible(true);
    }

}
