package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.UserDAO;
import model.UserData;
import result.UserResult;
import service.LoginService;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Objects;

public class LoginHandler {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public LoginHandler (AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public Object handleLogin(Request req, Response res) {
        LoginService service = new LoginService(authDAO, userDAO);

        var serializer = new Gson();

        UserData u = serializer.fromJson(req.body(), UserData.class);
        UserResult result = service.login(u);

        var json = serializer.toJson(result);

        // Success response	[200] { "username":"", "authToken":"" }
        if (result.message() == null || result.message().isEmpty()) {
            res.status(200);
            return json;
        }

        // Failure response	[401] { "message": "Error: unauthorized" }
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