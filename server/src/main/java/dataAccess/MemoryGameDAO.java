package dataAccess;

import model.GameData;

public class MemoryGameDAO implements GameDAO {
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
    }
    public void updateGame(GameData g) throws DataAccessException {
        // Update game in database
    }
}
