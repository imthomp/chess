package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import result.ClearResult;

public class ClearService {
    public ClearResult clear() {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.clear();
            AuthDAO authDAO = new AuthDAO();
            authDAO.clear();
            GameDAO gameDAO = new GameDAO();
            gameDAO.clear();
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new ClearResult(message);
        }
    }
}
