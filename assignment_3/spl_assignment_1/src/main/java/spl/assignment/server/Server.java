package spl.assignment.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import spl.assignment.encryption.EncrypterDecrypter;
import spl.assignment.encryption.NoEncryption;
import spl.assignment.utils.Communication;
import spl.assignment.utils.Logger;
import spl.assignment.utils.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.json.*;


public class Server {
	public static final String PASSWORD = "123456789";

	private final int port;
	

	private final AtomicBoolean run;
	private final EncrypterDecrypter encDec;
	//#if LOGGING
	private Logger logger;
	//#endif

	private final ArrayList<Message> messages;
	

	public Server(int port, EncrypterDecrypter encDec) {
		this.port = port;
		// If encryption is disabled, use NoEncryption	
		this.encDec = encDec;	
		this.run = new AtomicBoolean(true);
		this.messages = new ArrayList<>();
		//#if LOGGING
		this.logger = new Logger("server");
		this.logger.log("================ Server started =================");
		//#endif
	}

	public void stop() {
		this.run.set(false);
	}

	private boolean checkPassword(String attempt) {
		return attempt.equals(PASSWORD);
	}
	
	
	//#if LOGGING
	// Getters
	public Logger getLogger(){
		return this.logger;
	}
	//#endif

	/**
	 * 
	 * @param jsonRequest
	 * @return The response
	 */
	private String handleRequest(String jsonRequest) throws JSONException{
		JSONObject requestObj = new JSONObject(jsonRequest);

		String type = requestObj.getString("type");

		if (type.equals("sendMsg")) {
			JSONObject msgObj = requestObj.getJSONObject("message");
			this.messages.add(Message.fromJson(msgObj));
			// Log
			//#if LOGGING
			this.logger.log("Received message:" + msgObj.toString());
			//#endif
		}

		if (type.equals("stop")) {
			this.stop();
		}

		JSONArray responseObj = new JSONArray();

		for (Message msg : this.messages) {
			responseObj.put(msg.toJson());
		}

		return responseObj.toString();
	}

	public int getPort() {
		return port;
	}

	public void blockingStart() {
		try (ServerSocket srv = new ServerSocket(this.port)) {
			while (this.run.get()) {
				try {
					Socket socket = srv.accept();
					// Log
					//#if LOGGING
					this.logger.log("New connection accepted, socket: " + socket.toString());
					//#endif

					DataInputStream is = new DataInputStream(socket.getInputStream());
					DataOutputStream os = new DataOutputStream(socket.getOutputStream());

					//#if AUTHENTICATION
					String password = Communication.retrieveAndDecrypt(this.encDec, is);
					boolean passwordCorrect = this.checkPassword(password);
					//#else
//@					boolean passwordCorrect = true;
					//#endif

					if (passwordCorrect) {
						String jsonBody = Communication.retrieveAndDecrypt(this.encDec, is);
						String response = this.handleRequest(jsonBody);
						Communication.encryptAndSend(response, this.encDec, os);
					}

					socket.close();
				} catch (Exception e) {
					// Log
					//#if LOGGING
					this.logger.log("New connection refused, got error: " + e.getMessage());
					//#endif
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			//#if LOGGING
			this.logger.log("Server failed to start, got error: " + e.getMessage());
			//#endif
			e.printStackTrace();
		}
	}

}
