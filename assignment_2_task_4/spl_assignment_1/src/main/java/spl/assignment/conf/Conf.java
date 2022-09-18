package spl.assignment.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Conf {

    // Config variables
    public boolean color = true;
    public boolean encryption = true;
    public boolean loggingToFile = true;
    public boolean loggingToConsole = true;
    public boolean serverGUI = true;

    // Private constructor
    Conf() {
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path = Paths.get("").toAbsolutePath() + "\\src\\main\\java\\spl\\assignment\\conf\\";
        System.out.println(path);

        JSONParser parser = new JSONParser();
        try {
            // Read JSON file
            Object obj = parser.parse(new FileReader(path + "config.json"));
            JSONObject jsonobj = (JSONObject) obj;
            color = jsonobj.containsKey("colorMessage") ? (boolean) jsonobj.get("colorMessage") : color;
            encryption = jsonobj.containsKey("encryption") ? encryption : (boolean) jsonobj.get("encryption");
            loggingToFile = jsonobj.containsKey("loggingToFile") ? (boolean) jsonobj.get("loggingToFile")
                    : loggingToFile;
            loggingToConsole = jsonobj.containsKey("loggingToConsole") ? (boolean) jsonobj.get("loggingToConsole")
                    : loggingToConsole;
            serverGUI = jsonobj.containsKey("serverGUI") ? (boolean) jsonobj.get("serverGUI") : serverGUI;
        } catch (Exception e) {
            // defaults already set
            System.out.println("config.json NOT FOUND \nUsing defaults...");
        }
    }

    private Object parseEmployeeObject(JSONObject emp) {
        return null;
    }

    // Single instance variable
    private static Conf single_instance = null;

    // Static method to create instance of Conf class
    public static Conf getInstance() {
        if (single_instance == null)
            single_instance = new Conf();

        return single_instance;
    }
}
