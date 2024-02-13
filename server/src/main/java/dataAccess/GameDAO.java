package dataAccess;

import model.GameData;

public interface GameDAO {
    void createGame(GameData g) throws DataAccessException;
    void getGame(int gameID) throws DataAccessException;
    void listGames() throws DataAccessException;
    void clear() throws DataAccessException;
    void updateGame(GameData g) throws DataAccessException;
}
