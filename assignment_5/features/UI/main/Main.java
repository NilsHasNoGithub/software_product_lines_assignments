package main;

import client.Client;
import server.Server;

/**
 * TODO description
 */
public class Main {
	private static void startServerUi(Server server) {
		ServerGui gui = new ServerGui(server);
        gui.show();
	}
	
	private static void startClientUi(Client client) {
    	ClientGui gui = new ClientGui(client);
        gui.show();
    }
}