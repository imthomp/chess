package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import result.MessageResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public ClearHandler (AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Object handleClear(Request req, Response res) {
        ClearService service = new ClearService(authDAO, gameDAO, userDAO);
        MessageResult result = service.clear();
        var serializer = new Gson();
        var json = serializer.toJson(result);

        // Success response	[200]
        if (result.message() == null || result.message().isEmpty()) {
            res.status(200);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            res.status(500);
        }
        return json;
    }
}
