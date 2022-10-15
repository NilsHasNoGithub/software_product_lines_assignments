import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import server.Server;
import utils.Communication;
import client.Client;
import org.json.JSONArray;

public aspect Authentication {
	public static String PASSWORD = "123456789";
	
	pointcut serverHandleClientPC(Server server, DataInputStream is, DataOutputStream os) : execution(private void Server.handleClient(DataInputStream, DataOutputStream)) && args(is, os) && this(server);
	pointcut clientHandleConnectionPC(Client client, String requestString, DataInputStream is, DataOutputStream os): execution(private JSONArray Client.handleConnection(String, DataInputStream, DataOutputStream)) && args(requestString, is, os) && this(client);
	pointcut clientInitialization(Client client): initialization(public Client.new(..)) && this(client);
	
	void around(Server server, DataInputStream is, DataOutputStream os) throws IOException : serverHandleClientPC(server, is, os) {
		String attempt = Communication.retrieveAndDecrypt(server.getEncDec(), is);
		if (attempt.equals(PASSWORD)) {
			proceed(server, is, os);
		}
	}
	
	before(Client client, String requestString, DataInputStream is, DataOutputStream os) throws IOException : clientHandleConnectionPC(client, requestString, is, os) {
//		System.out.println("Connecting to server");
		Communication.encryptAndSend(client.getPassword(), client.getEncDec(), os);
	}
	
	after(Client client) : clientInitialization(client) {
		String passord = (String) JOptionPane.showInputDialog(null, "Password:", "Password prompt", JOptionPane.PLAIN_MESSAGE, null, null, PASSWORD);
		client.setPassword(passord);
	}
	
}