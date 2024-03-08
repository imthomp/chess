package dataAccessTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.GameDAO;
import dataAccess.object.sql.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Test;

public class GameTest {
    GameDAO gameDAO;

    GameTest() {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            System.out.println("Failed to initialize: " + e.getMessage());
        }
    }

    @Test
    public void positiveCreateGameTest() throws DataAccessException {
        gameDAO.createGame(1, "username");
    }

    @Test
    public void negativeCreateGameTest() throws DataAccessException {
        gameDAO.createGame(2, "username");
    }

    @Test
    public void positiveGetGameTest() throws DataAccessException {
        gameDAO.getGame(1);
    }

    @Test
    public void negativeGetGameTest() throws DataAccessException {
        gameDAO.getGame(1);
    }
    
    @Test
    public void positiveListGamesTest() throws DataAccessException {
        gameDAO.listGames();
    }

    @Test
    public void negativeListGamesTest() throws DataAccessException {
        gameDAO.listGames();
    }

    @Test
    public void positiveUpdateGameTest() throws DataAccessException {
        gameDAO.updateGame(new GameData(1, null, null, "username", null));
    }

    @Test
    public void negativeUpdateGameTest() throws DataAccessException {
        gameDAO.updateGame(new GameData(2, null, null, "username", null));
    }
    
    @Test
    public void clearTest() throws DataAccessException {
        gameDAO.clear();
    }
}
