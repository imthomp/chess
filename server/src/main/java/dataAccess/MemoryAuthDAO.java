package dataAccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    public void createAuth(AuthData a) throws DataAccessException {
        // Insert auth into database
    }
    public void getAuth(String authToken) throws DataAccessException {
        // Get auth from database
    }
    public void deleteAuth(String authToken) throws DataAccessException {
        // Delete auth from database
    }
    public void clear() throws DataAccessException {
        // Clear database
    }
}
