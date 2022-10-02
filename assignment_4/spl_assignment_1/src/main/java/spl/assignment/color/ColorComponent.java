package spl.assignment.color;

import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;

public class ColorComponent implements ChatColorComponent{

    // rgb colors
    private final JTextField rColorField;
    private final JTextField gColorField;
    private final JTextField bColorField;

    private final JPanel colorPanel;

    public ColorComponent() {
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

        colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
        colorPanel.setSize(200, 20);

        colorPanel.add(new JLabel("R: "));
        colorPanel.add(rColorField);
        colorPanel.add(new JLabel("G: "));
        colorPanel.add(gColorField);
        colorPanel.add(new JLabel("B: "));
        colorPanel.add(bColorField);
    }

    @Override
    public Optional<JComponent> getSelectionComponent() {
        return Optional.of(this.colorPanel);
    }

    @Override
    public synchronized Color getColor() {
        int r = Integer.parseInt(rColorField.getText());
        int g = Integer.parseInt(gColorField.getText());
        int b = Integer.parseInt(bColorField.getText());

        return new Color(r, g, b);
    }

    @Override
    public String getChatTextRepr() {
        Color c = this.getColor();
        return ", color (" + c.r + ", " + c.g + ", " + c.b + ")";
    }

    

    
}
