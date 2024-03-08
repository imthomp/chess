package dataAccessTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.sql.SQLAuthDAO;
import org.junit.jupiter.api.Test;

public class AuthTest {
    AuthDAO authDAO;

    AuthTest () {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            System.out.println("Failed to initialize: " + e.getMessage());
        }
    }

    @Test
    public void positiveCreateAuthTest() throws DataAccessException {
        authDAO.createAuth("username");
    }

    @Test
    public void negativeCreateAuthTest() throws DataAccessException {
        authDAO.createAuth("username");
    }

    @Test
    public void positiveGetAuthTest() throws DataAccessException {
        authDAO.getAuth("authToken");
    }

    @Test
    public void negativeGetAuthTest() throws DataAccessException {
        authDAO.getAuth("authToken");
    }

    @Test
    public void positiveDeleteAuthTest() throws DataAccessException {
        authDAO.deleteAuth("authToken");
    }

    @Test
    public void negativeDeleteAuthTest() throws DataAccessException {
        authDAO.deleteAuth("authToken");
    }

    @Test
    public void clearTest() throws DataAccessException {
        authDAO.clear();
    }
}
