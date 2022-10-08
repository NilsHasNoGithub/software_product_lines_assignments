package main;

/**
 * TODO description
 */
public class ClientCui {
	
	private String formatMessage(Message message) {
		String toPrint = message.getUsername();

        toPrint += ", color (" + message.getR() + ", " + message.getG() + ", " + message.getB() + ")";
        toPrint += ": " + message.getContent();
        
        return toPrint;
	}

}