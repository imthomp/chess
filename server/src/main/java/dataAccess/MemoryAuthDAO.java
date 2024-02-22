package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    HashMap<String, AuthData> auths;
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
        auths = new HashMap<String, AuthData>();
    }
}
