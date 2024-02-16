package dataAccess;

import model.AuthData;

import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {
    Collection<AuthData> auths;
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
        for (AuthData a : auths) {
            auths.remove(a);
        }
    }
}
