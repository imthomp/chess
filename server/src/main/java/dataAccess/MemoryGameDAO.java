package dataAccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games;

    public MemoryGameDAO() {
        games = new HashMap<Integer, GameData>();
    }

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
        games = new HashMap<Integer, GameData>();
    }

    public void updateGame(GameData g) throws DataAccessException {
        // Update game in database
    }
}
