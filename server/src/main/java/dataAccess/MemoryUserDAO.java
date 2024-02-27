package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.UserData;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String,UserData> users;

    public MemoryUserDAO() {
        users = new HashMap<String,UserData>();
    }

    public void createUser(UserData u) throws DataAccessException {
        // Insert user into database
        users.put(u.username(), u);
    }

    public UserData getUser(UserData u) throws DataAccessException {
        // Get user from database
        if (users == null) {
            return null;
        }
        return users.get(u.username());
    }

    public boolean checkUserCredentials(UserData u) throws DataAccessException {
        // TODO
        return false;
    }

    public void clear() throws DataAccessException {
        // Clear database
        users = new HashMap<String,UserData>();
    }

}
