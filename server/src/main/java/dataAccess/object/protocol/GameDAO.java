package dataAccess.object.protocol;

import dataAccess.exception.DataAccessException;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    GameData createGame(String authToken, String gameName) throws DataAccessException;
    void getGame(int gameID) throws DataAccessException;
    public HashSet<GameData> listGames(String username) throws DataAccessException;
    void clear() throws DataAccessException;
    void updateGame(GameData g) throws DataAccessException;
}
