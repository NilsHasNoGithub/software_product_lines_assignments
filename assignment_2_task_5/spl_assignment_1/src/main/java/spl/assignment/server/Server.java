package spl.assignment.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

	private final ArrayList<Message> messages;
	private final ArrayList<ServerObserver> observers;
	

	public Server(int port, EncrypterDecrypter encDec) {
		this.port = port;
		this.encDec = encDec;
		this.run = new AtomicBoolean(true);
		this.messages = new ArrayList<>();
		this.observers = new ArrayList<>();
		this.logger = new Logger("server");
		this.logger.log("================ Server started =================");
	}

	public synchronized void addObserver(ServerObserver observer) {
		this.observers.add(observer);
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
			Message message = Message.fromJson(msgObj);
			this.messages.add(message);
			this.notifyObserversMsg(message);
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

	private synchronized void notifyObserversMsg(Message msg) {
		for (ServerObserver observer : this.observers) {
			observer.messageReceived(msg);
		}
	}

	private synchronized void notifyObserversClient(Socket socket) {
		for (ServerObserver observer : this.observers) {
			observer.clientConnected(socket);
		}
	}

	private synchronized void notifyObserversError(Exception e) {
		for (ServerObserver observer : this.observers) {
			observer.errorEncountered(e);
		}
	}

	public void blockingStart() {
		try (ServerSocket srv = new ServerSocket(this.port)) {
			while (this.run.get()) {
				try {
					Socket socket = srv.accept();
					// Log
					this.notifyObserversClient(socket);

					DataInputStream is = new DataInputStream(socket.getInputStream());
					DataOutputStream os = new DataOutputStream(socket.getOutputStream());


					String password = Communication.retrieveAndDecrypt(this.encDec, is);

					if (this.checkPassword(password)) {
						String jsonBody = Communication.retrieveAndDecrypt(this.encDec, is);
						String response = this.handleRequest(jsonBody);
						Communication.encryptAndSend(response, this.encDec, os);
					}

					socket.close();
				} catch (Exception e) {
					// Log
					this.notifyObserversError(e);
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			this.logger.log("Server failed to start, got error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
