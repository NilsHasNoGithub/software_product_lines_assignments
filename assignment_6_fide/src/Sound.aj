import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import client.Client;
import utils.Message;

public aspect Sound {
	private int messageCount = 0;
	
//	pointcut resultHandleClient1(Client client, List<Message> result): execution(public List<Message> Client.requestMessageList()) && this(client);
//	pointcut resultHandleClient2(Client client, List<Message> result): execution(public List<Message> Client.sendMessage()) && this(client);
//
//	
//	before(Client client, List<Message> result) throws IOException: resultHandleClient1(client, result) {
//        updateMsgCountAndBeep(result);
//	}
//	
//	before(Client client, List<Message> result) throws IOException: resultHandleClient2(client, result) {
//        updateMsgCountAndBeep(result);
//	}
	
	private void updateMsgCountAndBeep(List<Message> msgs) throws IOException {
		int newCount = msgs.size();
		
		if (newCount > messageCount) {
			messageCount = newCount;
			playSound();
		}
	}
	
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
}