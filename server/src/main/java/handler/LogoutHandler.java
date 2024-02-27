package handler;

import dataAccess.object.protocol.AuthDAO;
import result.MessageResult;
import service.LogoutService;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Objects;

public class LogoutHandler {
    private final AuthDAO authDAO;

    public LogoutHandler (AuthDAO authDAO) {
        this.authDAO = authDAO;
    }
    public Object handleLogout(Request req, Response res) {
        LogoutService service = new LogoutService(authDAO);

        var serializer = new Gson();

        String authToken = req.headers("authorization");
        MessageResult result = service.logout(authToken);

        var json = serializer.toJson(result);

        // Success response	[200]
        if (result.message() == null || result.message().isEmpty()) {
            res.status(200);
        }

        // Failure response	[401] { "message": "Error: unauthorized" }
        else if (result.message().equals("Error: unauthorized")) {
            res.status(401);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            res.status(500);
        }
        return json;
    }
}