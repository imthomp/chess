package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import result.RegisterResult;
import service.LoginService;
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
        RegisterResult result = service.login(u);
        var json = serializer.toJson(result);

        // Success response	[200] { "username":"", "authToken":"" }
        if (result.message() == null || result.message().isEmpty()) {
            res.status(200);
            // TODO strip json of message
            return json;
        }

        // Failure response	[400] { "message": "Error: unauthorized" }
        if (Objects.equals(result.message(), "unauthorized")) {
            // TODO properly throw error somewhere
            res.status(400);
        }

        // Failure response	[500] { "message": "Error: description" }
        else {
            // TODO properly throw error somewhere
            res.status(500);
        }
        // TODO strip json of username, authToken
        return json;
    }
}