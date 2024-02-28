package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import result.ListGamesResult;
import service.ListGamesService;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class ListGamesHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesHandler (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object handleListGames(Request req, Response res) {
        ListGamesService service = new ListGamesService(authDAO, gameDAO);

        var serializer = new Gson();

        String authToken = req.headers("authorization");
        ListGamesResult result = service.listGames(authToken);

        var json = serializer.toJson(result);

        // Success response [200]
        if (result.message() == null) {
            res.status(200);
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