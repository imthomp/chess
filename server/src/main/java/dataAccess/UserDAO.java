package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData u) throws DataAccessException;
    void getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
