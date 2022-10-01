package spl.assignment.color;

import org.json.JSONArray;

public class SolidColor implements ChatColor {
    private Color color;

    public SolidColor(int r, int g, int b) {
        color = new Color(r, g, b);
    }

    public SolidColor() {
        this(0, 0, 0);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getR() {
        return color.r;
    }

    @Override
    public int getG() {
        return color.g;
    }

    @Override
    public int getB() {
        return color.b;
    }

    @Override
    public JSONArray toJSONArray() {
        return new JSONArray(new int[] { color.r, color.g, color.b });
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SolidColor))
            return false;

        SolidColor other = (SolidColor) obj;

        return other.getR() == color.r && other.getG() == color.g && other.getB() == color.b;
    }
}