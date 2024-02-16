package dataAccess;

import model.GameData;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    Collection<GameData> games;
    public void createGame(GameData g) throws DataAccessException {
        // Insert game into database
    }
    public void getGame(int gameID) throws DataAccessException {
        // Get game from database
    }
    public void listGames() throws DataAccessException {
        // Get all games from database
    }
    public void clear() throws DataAccessException {
        // Clear database
        for (GameData g : games) {
            games.remove(g);
        }
    }
    public void updateGame(GameData g) throws DataAccessException {
        // Update game in database
    }
}
