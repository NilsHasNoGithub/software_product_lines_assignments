package spl_assignment_1.spl_assignment_1;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import spl.assignment.client.Client;
import spl.assignment.encryption.EncrypterDecrypter;
import spl.assignment.encryption.Reverser;
import spl.assignment.main.Main;
import spl.assignment.server.Server;
import spl.assignment.utils.Logger;
import spl.assignment.utils.Message;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testServerClientCommunication() throws IOException, InterruptedException {

        EncrypterDecrypter encDec = Main.ENC_DEC;

        int port = 7123;
        String string = "0.0.0.0";

        final Server server = new Server(port, encDec);
        final Client client1 = new Client(port, string, encDec, "client1", Server.PASSWORD);
        final Client client2 = new Client(port, string, encDec, "client2", Server.PASSWORD);

        // Create loggers
        server.getLogger().log("Server log initialized");
        client1.getLogger().log("Client 1 log initialized");
        client2.getLogger().log("Client 2 log initialized");

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                server.blockingStart();
            }
        };

        Thread t = new Thread(serverTask);
        t.start();

        Message msg1 = new Message("Hello world!", "client1");
        Message msg2 = new Message("Hi, nice to meet u", "client2", 255, 0, 0);
        Message msg3 = new Message("Nice to meet you too", "client1");

        client1.sendMessage("Hello world!");
        client2.sendMessage("Hi, nice to meet u", 255, 0, 0);
        //
        List<Message> messages1 = client1.sendMessage("Nice to meet you too");
        List<Message> messages2 =  client1.requestMessageList();
        List<Message> messages3 = client2.stopServer();

        assertArrayEquals(messages1.toArray(), messages2.toArray());
        assertArrayEquals(messages2.toArray(), messages3.toArray());
        

        for (Message m: messages2) {
            System.out.println(m.toJson().toString());
        }

        assertEquals(msg1, messages2.get(0));
        assertEquals(msg2, messages2.get(1));
        assertEquals(msg3, messages2.get(2));


    }
}
