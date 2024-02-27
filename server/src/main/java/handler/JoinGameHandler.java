package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import result.MessageResult;
import service.JoinGameService;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Objects;

public class JoinGameHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameHandler (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public Object handleJoinGame(Request req, Response res) {
        JoinGameService service = new JoinGameService(authDAO, gameDAO);

        var serializer = new Gson();

        // TODO properly receive each of these parameters
        String authToken = serializer.fromJson(req.body(), String.class);
        String playerColor = serializer.fromJson(req.body(), String.class);
        String gameID = serializer.fromJson(req.body(), String.class);
        MessageResult result = service.joinGame(authToken, playerColor, gameID);

        var json = serializer.toJson(result);

        // Success response	[200]
        if (result.message() == null) {
            res.status(200);
        }

        // Failure response	[400] { "message": "Error: bad request" }
        if (Objects.equals(result.message(), "bad request")) {
            // TODO properly throw error somewhere
            res.status(400);
        }

        // Failure response	[401] { "message": "Error: unauthorized" }
        if (Objects.equals(result.message(), "unauthorized")) {
            // TODO properly throw error somewhere
            res.status(401);
        }

        // Failure response	[403] { "message": "Error: already taken" }
        if (Objects.equals(result.message(), "already taken")) {
            // TODO properly throw error somewhere
            res.status(401);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            // TODO properly throw error somewhere
            res.status(500);
        }
        return json;
    }
}