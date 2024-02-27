package dataAccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO {
    void createUser(UserData u) throws DataAccessException;
    UserData getUser(UserData u) throws DataAccessException;
    boolean checkUserCredentials(UserData u) throws DataAccessException;
    void clear() throws DataAccessException;
}
