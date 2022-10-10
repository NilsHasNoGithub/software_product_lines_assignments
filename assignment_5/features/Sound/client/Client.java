package client;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

import utils.Message;

/**
 * TODO description
 */
public class Client {
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