package dataAccess.object.sql;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.UserDAO;
import model.UserData;

import java.util.HashMap;

public class SQLUserDAO implements UserDAO {
    public void createUser(UserData u) {

    }

    public UserData getUser(UserData u) {
        return null;
    }

    public boolean checkUserCredentials(UserData u) {
        return false;
    }

    public void clear() throws DataAccessException {

    }
}
