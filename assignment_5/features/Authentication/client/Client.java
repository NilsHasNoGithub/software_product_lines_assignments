package client;

import java.io.DataOutputStream;
import java.io.IOException;

import encryption.EncrypterDecrypter;
import utils.Communication;
import utils.Logger;

/**
 * TODO description
 */
public class Client {
	private String password;
	
	public Client(int port, String host, EncrypterDecrypter encDec, String username, String password) {
        this.port = port;
        this.host = host;
        this.encDec = encDec;
        this.username = username;
        this.password = password;
        this.logger = new Logger("client_" + username);
        this.logger.log("================ Client started =================");
    }
	
	private void authenticate(DataOutputStream socketOs) throws IOException {
    	Communication.encryptAndSend(this.password, this.encDec, socketOs);
    }
}