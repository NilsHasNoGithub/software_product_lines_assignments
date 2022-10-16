package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import encryption.EncrypterDecrypter;
import utils.Communication;
import utils.Logger;
import utils.Message;

public class Client {  
    private int port;
    private String host;
    private EncrypterDecrypter encDec;
    private String username;
    private Logger logger;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Client(int port, String host, EncrypterDecrypter encDec, String username) {
        this.port = port;
        this.host = host;
        this.encDec = encDec;
        this.username = username;
        this.setPassword("");
        this.logger = new Logger("client_" + username);
        this.logger.log("================ Client started =================");
    }

    // Getters
    public Logger getLogger(){
		return this.logger;
	}

    private static JSONObject makeMsgRequest(Message msg) {
        JSONObject result = new JSONObject();

        result.put("type", "sendMsg");
        result.put("message", msg.toJson());

        return result;
    }

    private JSONObject singleFieldRequest(String type)  {
        JSONObject request = new JSONObject();
        request.put("type", type);
        return request;
    }

    public List<Message> requestMessageList() throws IOException {
    	List<Message> result = sendRequest(singleFieldRequest("requestMsgs"));
    	
    	return result;
    }

    public List<Message> stopServer() throws IOException {
        return sendRequest(singleFieldRequest("stop"));
    }

    public List<Message> sendMessage(String msg) throws IOException {
        List<Message> result = sendMessage(msg, 0,0,0);
    	
        return result;
    }   

    public List<Message> sendMessage(String content, int r, int g, int b) throws IOException {
        Message message = new Message(content, username, r, g, b);
        this.logger.log("Sending message: " + message.toString());
        return sendRequest(makeMsgRequest(message));
    }    
    
    private void authenticate(DataOutputStream socketOs) throws IOException {
    	
    }
    
    private List<Message> sendRequest(JSONObject request) throws IOException {

        JSONArray msgs;
        
        try {
        	Socket socket = new Socket(this.host, this.port);
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            msgs = handleConnection(request.toString(), is, os);
            
            socket.close();
        } catch (IOException e) {
            this.logger.log("IO error: " + e.toString());
            throw e;
        }
        
        return Message.messagesFromJson(msgs);
    }
    
    private JSONArray handleConnection(String requestString, DataInputStream is, DataOutputStream os) throws IOException {
        Communication.encryptAndSend(requestString, this.getEncDec(), os);
    	return new JSONArray(Communication.retrieveAndDecrypt(getEncDec(), is));
    }

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public EncrypterDecrypter getEncDec() {
		return encDec;
	}
}