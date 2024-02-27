package dataAccess.object.memory;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.UserDAO;
import model.UserData;

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
        String username = u.username();
        String password = u.password();
        UserData user = users.get(username);
        if (user == null) {
            return false;
        }
        return user.password().equals(password);
    }

    public void clear() throws DataAccessException {
        // Clear database
        users = new HashMap<String,UserData>();
    }

}
