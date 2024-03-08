package server;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.*;
import dataAccess.object.sql.*;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        try {
            Spark.port(desiredPort);

            Spark.staticFiles.location("web");

            AuthDAO authDAO = new SQLAuthDAO();
            GameDAO gameDAO = new SQLGameDAO();
            UserDAO userDAO = new SQLUserDAO();

            // Register your endpoints and handle exceptions here.
            Spark.delete("/db", (req, res) -> new ClearHandler(authDAO, gameDAO, userDAO).handleClear(res));
            Spark.post("/user", (req, res) -> new RegisterHandler(authDAO, userDAO).handleRegister(req, res));
            Spark.post("/session", (req, res) -> new LoginHandler(authDAO, userDAO).handleLogin(req, res));
            Spark.delete("/session", (req, res) -> new LogoutHandler(authDAO).handleLogout(req, res));
            Spark.get("/game", (req, res) -> new ListGamesHandler(authDAO, gameDAO).handleListGames(req, res));
            Spark.post("/game", (req, res) -> new CreateGameHandler(authDAO, gameDAO).handleCreateGame(req, res));
            Spark.put("/game", (req, res) -> new JoinGameHandler(authDAO, gameDAO).handleJoinGame(req, res));

            Spark.awaitInitialization();
            return Spark.port();
        } catch (DataAccessException e) {
            return Spark.port();
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
