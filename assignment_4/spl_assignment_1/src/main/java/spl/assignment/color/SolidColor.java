package spl.assignment.color;

public class SolidColor implements ChatColor {
    private Color color;

    public SolidColor(int r, int g, int b) {
        color = new Color(r, g, b);
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
        return new JSONArray(new int[] {color.r, color.g, color.b});
    }
}