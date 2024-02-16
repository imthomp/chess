package dataAccess;

import model.UserData;

import java.util.Collection;

public class MemoryUserDAO implements UserDAO {
    Collection<UserData> users;
    public void createUser(UserData u) throws DataAccessException {
        // Insert user into database
    }
    public void getUser(String username) throws DataAccessException {
        // Get user from database
    }
    public void clear() throws DataAccessException {
        // Clear database
        for (UserData u : users) {
            users.remove(u);
        }
    }
}
