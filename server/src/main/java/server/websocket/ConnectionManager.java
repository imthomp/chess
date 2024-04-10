package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    //TODO add multiple lobby functionality
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
//     public final ConcurrentHashMap<int, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session) {
//    public void add(String authToken, int gameID, Session) {
//        if (connections.get(gameID) == null) {
//            ConcurrentHashMap<String, Connection> map = new ConcurrentHashMap<>();
//            map.put(authToken, session);
//            connections.put(gameID, map);
//            // change how I access it later
//        }
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void notifyOthers(String excludeAuth, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.entrySet()) {
            if (c.getValue().session.isOpen()) {
                if (!c.getKey().equals(excludeAuth)) {
                    c.getValue().send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c.getValue());
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void notifyAll(ServerMessage notification) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                c.send(new Gson().toJson(notification));
            }
        }
    }

    public void notifyUser(String authToken, ServerMessage notification) throws IOException {
        var c = connections.get(authToken);
        if (c != null && c.session.isOpen()) {
            c.send(new Gson().toJson(notification));
        }
    }
}