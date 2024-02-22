package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    HashMap<String,UserData> users;
    public void createUser(UserData u) throws DataAccessException {
        // Insert user into database
        users.put(u.username(), u);
    }
    public UserData getUser(String username) throws DataAccessException {
        // Get user from database
        return users.get(username);
    }
    public void clear() throws DataAccessException {
        // Clear database
        users = new HashMap<String,UserData>();
    }
}
