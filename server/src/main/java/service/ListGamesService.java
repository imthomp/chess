package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.AuthData;
import model.GameData;
import result.ListGamesResult;

import java.util.Collection;

public class ListGamesService  {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesService (AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authData != null) {
                Collection<GameData> games = gameDAO.listGames();
                return new ListGamesResult(games, null);
            }
            else {
                throw new DataAccessException("unauthorized");
            }
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new ListGamesResult(null, "Error: " + message);
        }
    }
}
