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

import java.awt.Toolkit; 
import java.io.File; 
import java.io.InputStream; 
import java.net.URL; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.DataLine; 
import javax.sound.sampled.FloatControl; 

/**
 * TODO description
 */
public   class  Client {
	  
    private int port;

	
    private String host;

	
    private EncrypterDecrypter encDec;

	
    private String username;

	
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

	

    public Client(int port, String host, EncrypterDecrypter encDec, String username) {
        this.port = port;
        this.host = host;
        this.encDec = encDec;
        this.username = username;
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

	

     private List<Message>  requestMessageList__wrappee__Main  () throws IOException {
        return sendRequest(singleFieldRequest("requestMsgs"));
    }

	
	
	public List<Message> requestMessageList() throws IOException {
        List<Message> result = requestMessageList__wrappee__Main();
        
        updateMsgCountAndBeep(result);
    	return result;
    }

	

    public List<Message> stopServer() throws IOException {
        return sendRequest(singleFieldRequest("stop"));
    }

	

     private List<Message>  sendMessage__wrappee__Main  (String msg) throws IOException {
        return sendMessage(msg, 0,0,0);
    }

	


    public List<Message> sendMessage(String msg) throws IOException {
    	List<Message> result = sendMessage__wrappee__Main(msg);
    	updateMsgCountAndBeep(result);
    	return result;
    }

	   

    public List<Message> sendMessage(String content, int r, int g, int b) throws IOException {
        Message message = new Message(content, username, r, g, b);
        this.logger.log("Sending message: " + message.toString());
        return sendRequest(makeMsgRequest(message));
    }

	
	
	private void authenticate  (DataOutputStream socketOs) throws IOException {
    	Communication.encryptAndSend(this.password, this.encDec, socketOs);
    }

	
    
    private List<Message> sendRequest(JSONObject request) throws IOException {

        JSONArray msgs;
        
        try {
        	Socket socket = new Socket(this.host, this.port);
            DataInputStream is = new DataInputStream(socket.getInputStream());
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());


            authenticate(os);
            Communication.encryptAndSend(request.toString(), this.encDec, os);

            msgs = new JSONArray(Communication.retrieveAndDecrypt(encDec, is));
            socket.close();
        } catch (IOException e) {
            this.logger.log("IO error: " + e.toString());
            throw e;
        }
        
        return Message.messagesFromJson(msgs);
    }

	
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

	
	private int messageCount = 0;

	
	
	private static void playSound() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					// clip.open(AudioSystem.getAudioInputStream(new URL("https://nilsgolembiewski.nl/public_files/uploads/JqQuLHoi1xrEjPGmK2s3pfInbCBdVg/sound.wav")));
					System.out.println("Playing sound");
					AudioInputStream ais = AudioSystem.getAudioInputStream(new URL("https://nilsgolembiewski.nl/public_files/uploads/JqQuLHoi1xrEjPGmK2s3pfInbCBdVg/sound.wav"));
					DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
					Clip clip = (Clip) AudioSystem.getLine(info);
					clip.open(ais);
					clip.start();
					Thread.sleep(7000);
					clip.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}

	
	
	private void updateMsgCountAndBeep(List<Message> msgs) throws IOException {
		int newCount = msgs.size();
		
		if (newCount > messageCount) {
			messageCount = newCount;
			playSound();
		}
	}


}
