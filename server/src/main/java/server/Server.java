package server;

import dataAccess.*;
import handler.RegisterHandler;
import spark.*;
import handler.ClearHandler;

import java.nio.file.Paths;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        UserDAO userDAO = new MemoryUserDAO();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> new ClearHandler(authDAO, gameDAO, userDAO).handleClear(req, res));
        Spark.post("/user", (req, res) -> new RegisterHandler().handleRegister(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
