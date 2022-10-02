package spl.assignment.color;

import java.util.Optional;

import javax.swing.JComponent;

import org.json.JSONArray;

public interface ChatColorComponent {
    public Optional<JComponent> getSelectionComponent();

    public Color getColor();

}