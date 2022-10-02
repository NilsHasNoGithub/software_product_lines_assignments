package spl.assignment.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import spl.assignment.color.ChatColorComponent;
import spl.assignment.color.Color;
import spl.assignment.utils.Message;

public class ClientGui implements ClientUi {
    private final Client client;

    private final List<Message> prevMessages;

    private final JFrame mainFrame;
    private final JPanel mainPanel;

    // Text fields
    private final JTextPane chatField;
    private final JScrollPane chatScroll;
    private final JTextField newMessageField;

    private final ChatColorComponent chatColorComponent;

    private final JLabel infoLabel;

    private final JButton sendMsgButton;

    public ClientGui(Client client, ChatColorComponent chatColorComponent) {
        this.client = client;
        this.prevMessages = new ArrayList<>();
        this.chatColorComponent = chatColorComponent;

        mainFrame = new JFrame();
        mainFrame.setTitle("Client");
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainFrame.add(mainPanel);

        chatField = new JTextPane();
        chatField.setEditable(false);

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

        infoLabel = new JLabel(
                "Host: " + client.getAddress() + " Port: " + client.getPort() + " User: " + client.getUsername());
        mainPanel.add(infoLabel, BorderLayout.NORTH);
        chatScroll = new JScrollPane(chatField);
        mainPanel.add(chatScroll, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));

        Optional<JComponent> colorSelectionComponent = chatColorComponent.getSelectionComponent();
        if (colorSelectionComponent.isPresent()) {
            south.add(colorSelectionComponent.get());
        }
        
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

        Color color = this.chatColorComponent.getColor();

        try {
            client.sendMessage(message, color.r, color.g, color.b);
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

            Color c = message.getColor();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
                    new java.awt.Color(c.r, c.g, c.b));

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

    @Override
    public void show() {
        mainFrame.setVisible(true);
    }



}
