package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import result.MessageResult;
import server.JoinGameObject;
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

        String authToken = req.headers("authorization");
        JoinGameObject game = serializer.fromJson(req.body(), JoinGameObject.class);
        String playerColor = game.playerColor();
        Integer gameID = game.gameID();
        MessageResult result = service.joinGame(authToken, playerColor, gameID);

        var json = serializer.toJson(result);

        // Success response	[200]
        if (result.message() == null) {
            res.status(200);
        }

        // Failure response	[400] { "message": "Error: bad request" }
        else if (Objects.equals(result.message(), "Error: bad request")) {
            res.status(400);
        }

        // Failure response	[401] { "message": "Error: unauthorized" }
        else if (Objects.equals(result.message(), "Error: unauthorized")) {
            res.status(401);
        }

        // Failure response	[403] { "message": "Error: already taken" }
        else if (Objects.equals(result.message(), "Error: already taken")) {
            res.status(403);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            res.status(500);
        }
        return json;
    }
}