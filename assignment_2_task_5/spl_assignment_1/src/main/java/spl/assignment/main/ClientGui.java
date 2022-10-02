package spl.assignment.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.BorderLayout;

import spl.assignment.client.Client;
import spl.assignment.utils.Message;

public class ClientGui {
    private final Client client;

    private final List<Message> prevMessages;

    private final JFrame mainFrame;
    private final JPanel mainPanel;

    // Text fields
    private final JTextPane chatField;
    private final JScrollPane chatScroll;
    private final JTextField newMessageField;

    // rgb colors
    private final JTextField rColorField;
    private final JTextField gColorField;
    private final JTextField bColorField;

    
    private final JLabel infoLabel;

    private final JButton sendMsgButton;



    public ClientGui(Client client) {
        this.client = client;

        this.prevMessages = new ArrayList<>();

        mainFrame = new JFrame();
        mainFrame.setTitle("Client");
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainFrame.add(mainPanel);


        chatField = new JTextPane();
        chatField.setEditable(false);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
        colorPanel.setSize(200, 20);

        rColorField = new JTextField();
        rColorField.setText("0");
        rColorField.setColumns(2);
        rColorField.setSize(20, 15);
        
        gColorField = new JTextField();
        gColorField.setText("0");
        gColorField.setColumns(2);
        gColorField.setSize(20, 15);
        
        bColorField = new JTextField();
        bColorField.setText("0");
        bColorField.setColumns(2);
        bColorField.setSize(20, 15);

        InputVerifier coloVerifier = new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (value < 0 || value > 255) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            }
            
        };

        rColorField.setInputVerifier(coloVerifier);
        gColorField.setInputVerifier(coloVerifier);
        bColorField.setInputVerifier(coloVerifier);
        
        colorPanel.add(new JLabel("R: "));
        colorPanel.add(rColorField);
        colorPanel.add(new JLabel("G: "));
        colorPanel.add(gColorField);
        colorPanel.add(new JLabel("B: "));
        colorPanel.add(bColorField);

        

        JPanel newMessagePanel = new JPanel();
        newMessagePanel.setLayout(new BoxLayout(newMessagePanel, BoxLayout.X_AXIS));

        newMessageField = new JTextField();
        newMessageField.setColumns(20);
        newMessagePanel.add(newMessageField);

        sendMsgButton = new JButton();
        sendMsgButton.setText("Send");
        sendMsgButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        newMessagePanel.add(sendMsgButton);
        

        infoLabel = new JLabel("Host: " + client.getAddress() + " Port: " + client.getPort() + " User: " + client.getUsername());
        mainPanel.add(infoLabel, BorderLayout.NORTH);
        chatScroll = new JScrollPane(chatField);
        mainPanel.add(chatScroll, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(colorPanel);
        south.add(newMessagePanel);
        
        mainPanel.add(south, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
        
        Runnable refreshChat = new Runnable() {
            @Override
            public void run() {
                while (true) {
                        requestMessagesAndUpdate();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(refreshChat).start();




    }

    private synchronized void sendMessage() {
        String message = newMessageField.getText();
        newMessageField.setText("");
        int r = Integer.parseInt(rColorField.getText());
        int g = Integer.parseInt(gColorField.getText());
        int b = Integer.parseInt(bColorField.getText());
        
        try {
            client.sendMessage(message, r, g, b);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void requestMessagesAndUpdate() {
        try {
            List<Message> messages = client.requestMessageList();
            updateChat(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void updateChat(List<Message> messages) {
        if (prevMessages.size() == messages.size()) {
            return;
        }

        prevMessages.clear();
        prevMessages.addAll(messages);

        chatField.setText("");
        chatField.setEditable(true);
        for (Message message : messages) {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new java.awt.Color(message.getR(), message.getG(), message.getB()));

            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
            aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);


            chatField.setCaretPosition(chatField.getDocument().getLength());
            chatField.setCharacterAttributes(aset, false);
            chatField.replaceSelection(message.getUsername() + ": " + message.getContent() + "\n");
        }

        JScrollBar vertical = chatScroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        
        chatField.setEditable(false);
    }


    public void show() {
        mainFrame.setVisible(true);
    }


}
