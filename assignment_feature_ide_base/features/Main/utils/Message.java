package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.*;

public class Message {
    private String content;
    private String username;
    private int[] color;

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public int[] getColor() {
        return color;
    }

    public int getR() {
        return color[0];
    }

    public int getG() {
        return color[1];
    }

    public int getB() {
        return color[2];
    }

    public Message(String content, String username, int r, int g, int b) {
        this.content = content;
        this.username = username;
        this.color = new int[] { r, g, b };
    }

    public Message(String content, String username) {
        this.content = content;
        this.username = username;
        this.color = new int[] { 0, 0, 0 };
    }

    public JSONObject toJson() {
        JSONObject result = new JSONObject();

        result.put("content", this.content);
        result.put("username", this.username);
        result.put("color", new JSONArray(this.color));

        return result;
    }

    public static Message fromJson(JSONObject json) throws JSONException {
        String content = json.getString("content");
        String username = json.getString("username");
        JSONArray jsColor = json.getJSONArray("color");

        return new Message(content, username, jsColor.getInt(0), jsColor.getInt(1), jsColor.getInt(2));
    }

    public static List<Message> messagesFromJson(JSONArray json) throws JSONException {
        List<Message> result = new ArrayList<Message>();

        for (int i = 0; i < json.length(); i++) {
            result.add(Message.fromJson(json.getJSONObject(i)));
        }

        return result;
    }

    @Override
    public String toString() {
        return this.toJson().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        }

        Message other = (Message) obj;

        return this.username.equals(other.username) && this.content.equals(other.content)
                && Arrays.equals(this.color, other.color);
    }

}
