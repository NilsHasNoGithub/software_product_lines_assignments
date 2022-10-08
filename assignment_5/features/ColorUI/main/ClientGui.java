package main;

import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.Client;


public class ClientGui {
	
	// rgb colors
    private final JTextField rColorField;
    private final JTextField gColorField;
    private final JTextField bColorField;
	
	public ClientGui(Client client) {
//		original();
		
		JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
        colorPanel.setSize(200, 20);

        rColorField = new JTextField();
        rColorField.setText("0");
        rColorField.setColumns(2);
        rColorField.setSize(20, 15);
        
        gColorField = new JTextField();
        gColorField.setText("0");
        gColorField.setColumns(2);
        gColorField.setSize(20, 15);
        
        bColorField = new JTextField();
        bColorField.setText("0");
        bColorField.setColumns(2);
        bColorField.setSize(20, 15);

        InputVerifier coloVerifier = new InputVerifier() {

            @Override
            public boolean verify(JComponent input) {
                JTextField textField = (JTextField) input;
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (value < 0 || value > 255) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                return true;
            }
            
        };

        rColorField.setInputVerifier(coloVerifier);
        gColorField.setInputVerifier(coloVerifier);
        bColorField.setInputVerifier(coloVerifier);
        
        colorPanel.add(new JLabel("R: "));
        colorPanel.add(rColorField);
        colorPanel.add(new JLabel("G: "));
        colorPanel.add(gColorField);
        colorPanel.add(new JLabel("B: "));
        colorPanel.add(bColorField);
        south.add(colorPanel);
	}
	
	private synchronized void sendMessage() {
        String message = newMessageField.getText();
        newMessageField.setText("");
        
        int r = Integer.parseInt(rColorField.getText());
        int g = Integer.parseInt(gColorField.getText());
        int b = Integer.parseInt(bColorField.getText());
        
        try {
            client.sendMessage(message, 0, 0, 0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}