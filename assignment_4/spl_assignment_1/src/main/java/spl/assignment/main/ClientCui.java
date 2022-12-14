package spl.assignment.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spl.assignment.client.Client;
import spl.assignment.color.ChatColorComponent;
import spl.assignment.color.Color;
import spl.assignment.utils.Message;

public class ClientCui implements ClientUi {
    private final Client client;

    private final List<Message> prevMessages;

    private final ChatColorComponent chatColorComponent;

    public ClientCui(Client client, ChatColorComponent chatColorComponent) {
        this.client = client;

        this.chatColorComponent = chatColorComponent;

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

            toPrint += this.chatColorComponent.getChatTextRepr();
            toPrint += ": " + message.getContent();

            System.out.println(toPrint);
        }
    }

    @Override
    public void show() {
    }

}
