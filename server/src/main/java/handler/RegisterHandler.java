package handler;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.UserDAO;
import model.UserData;
import result.UserResult;
import service.RegisterService;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.Objects;

public class RegisterHandler {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public RegisterHandler (AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public Object handleRegister(Request req, Response res) {
        RegisterService service = new RegisterService(authDAO, userDAO);

        var serializer = new Gson();

        UserData u = serializer.fromJson(req.body(), UserData.class);
        UserResult result = service.register(u);

        var json = serializer.toJson(result);

        // Success response	[200] { "username":"", "authToken":"" }
        if (result.message() == null) {
            res.status(200);
            return json;
        }

        // Failure response	[400] { "message": "Error: bad request" }
        else if (Objects.equals(result.message(), "Error: bad request")) {
            res.status(400);
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
