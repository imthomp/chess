package handler;

import com.google.gson.Gson;
import model.UserData;
import result.RegisterResult;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handleRegister(Request req, Response res) {
        RegisterService service = new RegisterService();
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        String email = req.queryParams("email");
        UserData u = new UserData(username, password, email);

        RegisterResult result = service.createUser(u);

        var serializer = new Gson();
        var json = serializer.toJson(result);

        // Success response	[200] { "username":"", "authToken":"" }
        if (result.message().isEmpty()) {
            res.status(200);
            return json;
        }

        // Failure response	[400] { "message": "Error: bad request" }
        if (result.message().equals("Error: bad request")) {
            res.status(400);
            return json;
        }

        // Failure response	[403] { "message": "Error: already taken" }
        if (result.message().equals("Error: already taken")) {
            res.status(403);
            return json;
        }

        // Failure response	[500] { "message": "Error: description" }
        res.status(500);
        return json;
    }
}
