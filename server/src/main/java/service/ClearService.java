package service;

import dataAccess.*;
import result.ClearResult;

public class ClearService {
    public ClearResult clear() {
        try {
            UserDAO userDAO = new MemoryUserDAO();
            userDAO.clear();
            AuthDAO authDAO = new MemoryAuthDAO();
            authDAO.clear();
            GameDAO gameDAO = new MemoryGameDAO();
            gameDAO.clear();
            return new ClearResult("");
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new ClearResult("Error:" + message);
        }
    }
}
