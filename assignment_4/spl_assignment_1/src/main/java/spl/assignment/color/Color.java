package spl.assignment.color;

import org.json.JSONArray;

public class Color {
    public int r;
    public int g;
    public int b;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public JSONArray toJsonArray() {
        return new JSONArray(new int[] { this.r, this.g, this.b });
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Color)) {
            return false;
        }

        Color other = (Color) obj;
        
        return this.r == other.r && this.g == other.g && this.b == other.b;
    }
}