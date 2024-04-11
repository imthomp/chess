package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
     public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        if (connections.get(gameID) == null) {
            ConcurrentHashMap<String, Connection> map = new ConcurrentHashMap<>();
            Connection connection = new Connection(authToken, session);
            map.put(authToken, connection);
            connections.put(gameID, map);
        } else {
            Connection connection = new Connection(authToken, session);
            connections.get(gameID).put(authToken, connection);
        }
    }

    public void remove(String authToken, int gameID) {
        connections.get(gameID).remove(authToken);
    }

    public void notifyOthers(String excludeAuth, ServerMessage message, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.get(gameID).entrySet()) {
            if (c.getValue().session.isOpen()) {
                if (!c.getKey().equals(excludeAuth)) {
                    c.getValue().send(new Gson().toJson(message));
                }
            } else {
                removeList.add(c.getValue());
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.get(gameID).remove(c.authToken);
        }
    }

    public void notifyAll(ServerMessage message, int gameID) throws IOException {
        for (var c : connections.get(gameID).values()) {
            if (c.session.isOpen()) {
                c.send(new Gson().toJson(message));
            }
        }
    }

    public void notifyUser(String authToken, ServerMessage message, int gameID) throws IOException {
        var c = connections.get(gameID).get(authToken);
        if (c != null && c.session.isOpen()) {
            c.send(new Gson().toJson(message));
        }
    }
}