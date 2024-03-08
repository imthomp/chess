package dataAccessTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.UserDAO;
import dataAccess.object.sql.SQLUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

public class UserTest {
    UserDAO userDAO;

    UserTest() {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            System.out.println("Failed to initialize: " + e.getMessage());
        }
    }

    @Test
    public void positiveCreateUserTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
    }

    @Test
    public void negativeCreateUserTest() throws DataAccessException {
        userDAO.createUser(new UserData("username2", "password", "email"));
    }

    @Test
    public void positiveGetUserTest() throws DataAccessException {
        userDAO.getUser(new UserData("username", "password", "email"));
    }

    @Test
    public void negativeGetUserTest() throws DataAccessException {
        userDAO.getUser(new UserData("username", "password", "email"));
    }
    
    @Test
    public void positiveCheckUserCredentialsTest() throws DataAccessException {
        userDAO.checkUserCredentials(new UserData("username", "password", "email"));
    }

    @Test
    public void negativeCheckUserCredentialsTest() throws DataAccessException {
        userDAO.checkUserCredentials(new UserData("username", "password", "email"));
    }
    
    @Test
    public void clearTest() throws DataAccessException {
        userDAO.clear();
    }
}
