package dataAccess.object.protocol;

import dataAccess.exception.DataAccessException;
import model.UserData;

public interface UserDAO {
    void createUser(UserData u) throws DataAccessException;
    UserData getUser(UserData u) throws DataAccessException;
    boolean checkUserCredentials(UserData u) throws DataAccessException;
    void clear() throws DataAccessException;
}
