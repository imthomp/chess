package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import model.AuthData;
import model.GameData;
import result.ListGamesResult;

import java.util.HashSet;

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
                String username = authData.username();
                HashSet<GameData> games = gameDAO.listGames(username);
                return new ListGamesResult(games, null);
            }
            else {
                return new ListGamesResult(null, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new ListGamesResult(null, "Error:" + message);
        }
    }
}
