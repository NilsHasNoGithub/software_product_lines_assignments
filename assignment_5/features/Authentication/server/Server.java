package server;

import java.io.DataInputStream;

import utils.Communication;

/**
 * TODO description
 */
public class Server {
	public static final String PASSWORD = "123456789";
	
	private boolean handleNewConnection(DataInputStream socketIs) throws IOException {
		String attempt = Communication.retrieveAndDecrypt(this.encDec, socketIs);
		return attempt.equals(PASSWORD);
	}
}