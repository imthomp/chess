package dataAccess.object.memory;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.GameDAO;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games;

    public MemoryGameDAO() {
        games = new HashMap<Integer, GameData>();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        // Create game in database
        return null;
    }

//    public void createGame(GameData g) throws DataAccessException {
//        // Insert game into database
//        games.put(g.gameID(), g);
//    }

    public void getGame(int gameID) throws DataAccessException {
        // Get game from database
    }

    public HashSet<GameData> listGames(String username) throws DataAccessException {
        // Get all games from database for a specific user
        HashSet <GameData> userGames = new HashSet<GameData>();
        for (GameData g : games.values()) {
            if (g.whiteUsername().equals(username) || g.blackUsername().equals(username)) {
                userGames.add(g);
            }
        }
        return userGames;
    }

    public void clear() throws DataAccessException {
        // Clear database
        games = new HashMap<Integer, GameData>();
    }

    public void updateGame(GameData g) throws DataAccessException {
        // Update game in database
    }
}
