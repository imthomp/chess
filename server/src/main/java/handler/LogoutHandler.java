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

        String authToken = serializer.fromJson(req.body(), String.class);
        MessageResult result = service.logout(authToken);

        var json = serializer.toJson(result);

        // Success response	[200]
        if (result.message() == null || result.message().isEmpty()) {
            res.status(200);
        }

        // Failure response	[401] { "message": "Error: unauthorized" }
        if (Objects.equals(result.message(), "unauthorized")) {
            // TODO properly throw error somewhere
            res.status(400);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            // TODO properly throw error somewhere
            res.status(500);
        }
        return json;
    }
}