package spl.assignment.server;

import java.io.IOException;
import java.lang.StackWalker.Option;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import spl.assignment.encryption.EncrypterDecrypter;
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
	private Logger logger;
	private Optional<Authenticator> authenticator;

	private final ArrayList<Message> messages;
	
	public Server(int port, EncrypterDecrypter encDec) {
		this(port, encDec, Optional.empty());
	}

	public Server(int port, EncrypterDecrypter encDec, Authenticator auth) {
		this(port, encDec, Optional.of(auth));
	}

	private Server(int port, EncrypterDecrypter encDec, Optional<Authenticator> auth) {
		this.port = port;
		this.encDec = encDec;
		this.authenticator = auth;
		this.run = new AtomicBoolean(true);
		this.messages = new ArrayList<>();
		this.logger = new Logger("server");
		this.logger.log("================ Server started =================");
	}

	public void stop() {
		this.run.set(false);
	}

	private boolean checkPassword(String attempt) {
		return attempt.equals(PASSWORD);
	}

	// Getters
	public Logger getLogger(){
		return this.logger;
	}

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
			this.logger.log("Received message:" + msgObj.toString());
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
					this.logger.log("New connection accepted, socket: " + socket.toString());

					DataInputStream is = new DataInputStream(socket.getInputStream());
					DataOutputStream os = new DataOutputStream(socket.getOutputStream());

					boolean authSucces;
					
					if (this.authenticator.isPresent()) {
						String password = Communication.retrieveAndDecrypt(this.encDec, is);
						authSucces = this.authenticator.get().checkPassword(password);
					} else {
						authSucces = true;
					}
					

					if (authSucces) {
						String jsonBody = Communication.retrieveAndDecrypt(this.encDec, is);
						String response = this.handleRequest(jsonBody);
						Communication.encryptAndSend(response, this.encDec, os);
					}

					socket.close();
				} catch (Exception e) {
					// Log
					this.logger.log("New connection refused, got error: " + e.getMessage());
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			this.logger.log("Server failed to start, got error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
