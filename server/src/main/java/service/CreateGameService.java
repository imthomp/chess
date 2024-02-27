package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.AuthData;
import model.GameData;
import result.GameResult;

import java.util.UUID;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public GameResult createGame(String authToken, String gameName) {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authToken == null || authData == null) {
                throw new DataAccessException("unauthorized");
            }
            else if (gameName == null) {
                throw new DataAccessException("bad request");
            }
            Integer gameID = UUID.randomUUID().hashCode();
            gameDAO.createGame(gameID, gameName);
            return new GameResult(gameID, null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new GameResult(null, "Error: " + message);
        }
    }
}
