package dataAccess.object.memory;

import dataAccess.object.protocol.AuthDAO;
import dataAccess.exception.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> auths;

    public MemoryAuthDAO() {
        auths = new HashMap<>();
    }

    public String createAuth(String username) {
        // Insert auth into database
        String authToken = UUID.randomUUID().toString();
        auths.put(authToken, new AuthData(authToken, username));
        return authToken;
    }

    public AuthData getAuth(String authToken) {
        // Get auth from database
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        // Delete auth from database
        auths.remove(authToken);
    }

    public void clear() throws DataAccessException {
        // Clear database
        auths = new HashMap<>();
    }
}
