package spl.assignment.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import spl.assignment.authentication.Authenticator;
import spl.assignment.encryption.EncrypterDecrypter;
import spl.assignment.main.Main;
import spl.assignment.utils.Communication;
import spl.assignment.utils.Logger;
import spl.assignment.utils.Message;

public class Client {
    private int port;
    private String host;
    private EncrypterDecrypter encDec;
    private String username;
    private Authenticator authenticator;
    private Logger logger;

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Client(int port, String host, EncrypterDecrypter encDec, String username, Authenticator authenticator) {
        this.port = port;
        this.host = host;
        this.encDec = encDec;
        this.username = username;
        this.authenticator = authenticator;
        this.logger = new Logger("client_" + username);
        this.logger.log("================ Client started =================");
    }

    // Getters
    public Logger getLogger() {
        return this.logger;
    }

    private static JSONObject makeMsgRequest(Message msg) {
        JSONObject result = new JSONObject();

        result.put("type", "sendMsg");
        result.put("message", msg.toJson());

        return result;
    }

    private JSONObject singleFieldRequest(String type) {
        JSONObject request = new JSONObject();
        request.put("type", type);
        return request;
    }

    public List<Message> requestMessageList() throws IOException {
        return sendRequest(singleFieldRequest("requestMsgs"));
    }

    public List<Message> stopServer() throws IOException {
        return sendRequest(singleFieldRequest("stop"));
    }

    public List<Message> sendMessage(String msg) throws IOException {
        return sendMessage(msg, 0, 0, 0);
    }

    public List<Message> sendMessage(String content, int r, int g, int b) throws IOException {
        Message message = new Message(content, username, r, g, b);
        this.logger.log("Sending message: " + message.toString());
        return sendRequest(makeMsgRequest(message));
    }

    private List<Message> sendRequest(JSONObject request) throws IOException {

        JSONArray msgs;

        try (Socket socket = new Socket(this.host, this.port)) {
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());

            Communication.encryptAndSend(this.authenticator.getAuthString(), this.encDec, os);

            Communication.encryptAndSend(request.toString(), this.encDec, os);

            msgs = new JSONArray(Communication.retrieveAndDecrypt(encDec, is));

        } catch (IOException e) {
            this.logger.log("IO error: " + e.toString());
            throw e;
        }

        return Message.messagesFromJson(msgs);
    }

}
