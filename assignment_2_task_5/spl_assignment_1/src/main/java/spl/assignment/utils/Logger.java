package spl.assignment.utils;

import java.io.File; // Import the File class
import java.io.FileWriter;
import java.io.IOException; // Import the IOException class to handle errors
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import spl.assignment.server.ServerObserver;


/**
 * Implements thread safe logger
 */
public class Logger implements ServerObserver{

    private final String fileName;
    private final List<String> logs;


    // Constructor
    public Logger(String fileName) {
        this.fileName = "logs/" + fileName  + ".txt";
        this.logs = new ArrayList<>();
    }

    /**
     * Collects all logs and returns them as string
     */
    public synchronized String getFullLog() {
        StringBuilder sb = new StringBuilder();

        for (String log : this.logs) {
            sb.append(log);
        }

        return sb.toString();
    }

    private synchronized void createFolder() {
        File file = new File(this.fileName);
        String folder = file.getParent();

        File folderFile = new File(folder);
        if (!folderFile.isDirectory()) {
            folderFile.mkdirs();
        }
    }

    // Write to file
    public synchronized void log(String msg) {
        this.createFolder();
        this.logs.add(msg + "\n");
        try (FileWriter myWriter = new FileWriter(this.fileName, true)) {
            myWriter.write(msg);
            myWriter.write(System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageReceived(Message msg) {
        this.log("Message received: " + msg.toString());
    }

    @Override
    public void clientConnected(Socket socket) {
        this.log("Client connected: " + socket.toString());
    }

    @Override
    public void errorEncountered(Exception e) {
        this.log("Error encountered: " + e.toString());
    }
}
