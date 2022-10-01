package spl.assignment.color;

public interface ChatColor {
    public Color getColor();

    public int getR();
    public int getG();
    public int getB();

    public JSONArray toJSONArray();
}