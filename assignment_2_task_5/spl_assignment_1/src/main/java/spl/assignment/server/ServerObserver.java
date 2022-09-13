package spl.assignment.server;



import java.net.Socket;

import spl.assignment.utils.Message;

public interface ServerObserver {
    public default void eventOccurred() {}
    public default void messageReceived(Message msg) {}
    public default void clientConnected(Socket socket) {}
    public default void errorEncountered(Exception e) {}
}