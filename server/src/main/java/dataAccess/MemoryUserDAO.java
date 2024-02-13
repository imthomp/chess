package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    public void createUser(UserData u) throws DataAccessException {
        // Insert user into database
    }
    public void getUser(String username) throws DataAccessException {
        // Get user from database
    }
    public void clear() throws DataAccessException {
        // Clear database
    }
}
