package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.AuthData;
import result.MessageResult;

public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
    public MessageResult joinGame(String authToken, String playerColor, String gameID) {
        try {
            //TODO
            AuthData authData = authDAO.getAuth(authToken);
            return new MessageResult(null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new MessageResult("Error:" + message);
        }
    }
}
