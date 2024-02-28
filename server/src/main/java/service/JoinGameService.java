package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.AuthData;
import model.GameData;
import result.MessageResult;

import java.util.Objects;

public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public MessageResult joinGame(String authToken, String playerColor, Integer gameID) {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authToken == null || authData == null) {
                throw new DataAccessException("unauthorized");
            }
            String username = authData.username();
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData == null) {
                throw new DataAccessException("bad request");
            }
            if (playerColor == null) {
                GameData newGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(newGameData);
            }
            else if (Objects.equals(playerColor, "WHITE")) {
                if (gameData.whiteUsername() != null) {
                    throw new DataAccessException("already taken");
                }
                GameData newGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(newGameData);
            }
            else if (playerColor.equals("BLACK")) {
                if (gameData.blackUsername() != null) {
                    throw new DataAccessException("already taken");
                }
                GameData newGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                gameDAO.updateGame(newGameData);
            }
            return new MessageResult(null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new MessageResult("Error: " + message);
        }
    }
}
