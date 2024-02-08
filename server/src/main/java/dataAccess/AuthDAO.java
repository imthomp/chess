package dataAccess;

import model.AuthData;

public class AuthDAO {
    void createAuth(AuthData a) throws DataAccessException {
        // Insert auth into database
    }
    void getAuth(String authToken) throws DataAccessException {
        // Get auth from database
    }
    void deleteAuth(String authToken) throws DataAccessException {
        // Delete auth from database
    }
    void clear() throws DataAccessException {
        // Clear database
    }
}
