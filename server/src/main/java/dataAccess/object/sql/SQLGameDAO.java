package dataAccess.object.sql;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.GameDAO;
import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    public void createGame(Integer gameID, String gameName) {

    }

    public GameData getGame(int gameID) {
        return null;
    }

    public Collection<GameData> listGames() {
        return null;
    }

    public void clear() throws DataAccessException {

    }

    public void updateGame(GameData g) {

    }
}
