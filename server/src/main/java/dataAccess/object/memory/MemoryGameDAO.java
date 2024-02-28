package dataAccess.object.memory;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games;

    public MemoryGameDAO() {
        games = new HashMap<>();
    }

    public void createGame(Integer gameID, String gameName) {
        // Create game in database
        GameData game = new GameData(gameID, null, null, gameName, null);
        games.put(gameID, game);
    }

    public GameData getGame(int gameID) {
        // Get game from database
        return games.get(gameID);
    }

    public Collection<GameData> listGames() {
        // Get all games from database for a specific user
        return games.values();
    }

    public void clear() throws DataAccessException {
        // Clear database
        games = new HashMap<>();
    }

    public void updateGame(GameData g) {
        // Update game in database
        games.put(g.gameID(), g);
    }
}
