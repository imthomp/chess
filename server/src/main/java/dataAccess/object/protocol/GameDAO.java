package dataAccess.object.protocol;

import dataAccess.exception.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(Integer gameID, String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void clear() throws DataAccessException;
    void updateGame(GameData g) throws DataAccessException;
}
