package dataAccess.object.sql;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    public String createAuth(String username) throws DataAccessException {
        return null;
    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void deleteAuth(String authToken) {

    }

    public void clear() throws DataAccessException {

    }
}