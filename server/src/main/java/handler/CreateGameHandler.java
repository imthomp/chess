package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import result.GameResult;
import service.CreateGameService;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class CreateGameHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameHandler (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object handleCreateGame(Request req, Response res) {
        CreateGameService service = new CreateGameService(authDAO, gameDAO);

        var serializer = new Gson();

        String authToken = serializer.fromJson(req.body(), String.class);
        String gameName = serializer.fromJson(req.body(), String.class);
        GameResult result = service.createGame(authToken, gameName);

        var json = serializer.toJson(result);

        // Success response [200]
        if (result.message() == null) {
            res.status(200);
        }

        // Failure response [400] { "message": "Error: bad request" }
        if (Objects.equals(result.message(), "bad request")) {
            res.status(400);
        }

        // Failure response [401] { "message": "Error: unauthorized" }
        if (Objects.equals(result.message(), "unauthorized")) {
            res.status(401);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            res.status(500);
        }
        return json;
    }
}