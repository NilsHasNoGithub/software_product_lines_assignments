package spl.assignment.utils;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.*;

import spl.assignment.color.ChatColorComponent;
import spl.assignment.color.Color;
import spl.assignment.color.NoColorComponent;

public class Message {
    private String content;
    private String username;
    private Color color;

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Color getColor() {
        return color;
    }

    // public int getR() {
    //     return color.orElse(null).getR();
    // }

    // public int getG() {
    //     return color.orElse(null).getG();
    // }

    // public int getB() {
    //     return color.orElse(null).getB();
    // }

    public Message(String content, String username, int r, int g, int b) {
        this.content = content;
        this.username = username;
        this.color = new Color(r, g, b);
    }

    public Message(String content, String username) {
        this.content = content;
        this.username = username;
        this.color = new Color(0, 0, 0);
    }

    public JSONObject toJson() {
        JSONObject result = new JSONObject();

        result.put("content", this.content);
        result.put("username", this.username);
        result.put("color", color.toJsonArray());

        return result;
    }

    public static Message fromJson(JSONObject json) throws JSONException {
        String content = json.getString("content");
        String username = json.getString("username");
        JSONArray jsColor = json.getJSONArray("color");

        return new Message(content, username, jsColor.getInt(0), jsColor.getInt(1), jsColor.getInt(2));
    }

    public static List<Message> messagesFromJson(JSONArray json) throws JSONException {
        List<Message> result = new ArrayList<>();

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

        return this.username.equals(other.username) &&
                this.content.equals(other.content) &&
                this.color.equals(other.color);
    }

}
