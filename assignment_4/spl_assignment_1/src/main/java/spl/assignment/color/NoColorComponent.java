package spl.assignment.color;

import java.util.Optional;

import javax.swing.JComponent;

import org.json.JSONArray;

public class NoColorComponent implements ChatColorComponent {
    private Color color;

    public NoColorComponent(int r, int g, int b) {
        color = new Color(r, g, b);
    }

    public NoColorComponent() {
        this(0, 0, 0);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NoColorComponent)) {
            return false;
        }

        NoColorComponent other = (NoColorComponent) obj;

        return other.color.equals(this.color);
    }

    @Override
    public Optional<JComponent> getSelectionComponent() {
        return Optional.empty();
    }
}