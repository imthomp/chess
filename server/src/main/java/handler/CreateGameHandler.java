package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.GameData;
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

        String authToken = req.headers("authorization");
        GameData game = serializer.fromJson(req.body(), GameData.class);
        String gameName = game.gameName();

        GameResult result = service.createGame(authToken, gameName);

        var json = serializer.toJson(result);

        // Success response [200]
        if (result.message() == null) {
            res.status(200);
        }

        // Failure response [400] { "message": "Error: bad request" }
        else if (Objects.equals(result.message(), "Error: bad request")) {
            res.status(400);
        }

        // Failure response [401] { "message": "Error: unauthorized" }
        else if (Objects.equals(result.message(), "Error: unauthorized")) {
            res.status(401);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            res.status(500);
        }
        return json;
    }
}