package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.*;
import result.MessageResult;

public class ClearService  {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public ClearService (AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public MessageResult clear() {
        try {
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
            return new MessageResult(null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new MessageResult("Error:" + message);
        }
    }
}
