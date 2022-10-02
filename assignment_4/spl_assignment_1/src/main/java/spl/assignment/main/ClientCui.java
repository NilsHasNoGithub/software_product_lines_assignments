package spl.assignment.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spl.assignment.client.Client;
import spl.assignment.utils.Message;

public class ClientCui implements ClientUi {
    private final Client client;

    private final List<Message> prevMessages;

    public ClientCui(Client client) {
        this.client = client;

        this.prevMessages = new ArrayList<>();

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

    private synchronized void updateChat(List<Message> messages) {
        if (prevMessages.size() == messages.size()) {
            return;
        }

        prevMessages.clear();
        prevMessages.addAll(messages);

        for (Message message : messages) {
            String toPrint = message.getUsername();

            // #if COLOR
            toPrint += ", color (" + message.getR() + ", " + message.getG() + ", " + message.getB() + ")";
            // #endif
            toPrint += ": " + message.getContent();

            System.out.println(toPrint);
        }
    }

    @Override
    public void show() {
    }

}
