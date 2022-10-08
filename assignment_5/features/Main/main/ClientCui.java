package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.Client;
import utils.Message;

public class ClientCui {
    private final Client client;

    private final List<Message> prevMessages;

    public ClientCui(Client client) {
        this.client = client;

        this.prevMessages = new ArrayList<Message>();

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

    private void requestMessagesAndUpdate() {
        try {
            List<Message> messages = client.requestMessageList();
            updateChat(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String formatMessage(Message message) {
    	String toPrint = message.getUsername();
        toPrint += ": " + message.getContent();
        
        return toPrint;
    }

    private synchronized void updateChat(List<Message> messages) {
        if (prevMessages.size() == messages.size()) {
            return;
        }

        prevMessages.clear();
        prevMessages.addAll(messages);

        for (Message message : messages) {
            System.out.println(formatMessage(message));
        }
    }

    public void show() {
    }

}
