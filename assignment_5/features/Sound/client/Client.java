package client;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import utils.Message;

/**
 * TODO description
 */
public class Client {
	private int messageCount = 0;
	
	private static void playSound() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new URL("https://nilsgolembiewski.nl/public_files/uploads/JqQuLHoi1xrEjPGmK2s3pfInbCBdVg/sound.wav")));
			clip.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateMsgCountAndBeep(List<Message> msgs) throws IOException {
		int newCount = msgs.size();
		
		if (newCount > messageCount) {
			messageCount = newCount;
			playSound();
		}
	}
	
	public List<Message> requestMessageList() throws IOException {
        List<Message> result = original();
        
        updateMsgCountAndBeep(result);
    	return result;
    }


    public List<Message> sendMessage(String msg) throws IOException {
    	List<Message> result = original(msg);
    	updateMsgCountAndBeep(result);
    	return result;
    }   
}