package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.AuthData;
import model.GameData;
import result.GameResult;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public GameResult createGame(String authToken, String gameName) {
        try {
            AuthData user = authDAO.getAuth(authToken);
            String username = user.username();
            GameData game = gameDAO.createGame(authToken, gameName);
            return new GameResult(game.gameID(), authToken);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new GameResult(null, "Error:" + message);
        }
    }
}
