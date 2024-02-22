package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;
import result.RegisterResult;
import dataAccess.MemoryUserDAO;

public class RegisterService {
    public RegisterResult getUser(String username) {
        try {
            UserDAO userDAO = new MemoryUserDAO();
            UserData u = userDAO.getUser(username);
            return new RegisterResult("");
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new RegisterResult(message);
        }
    }

    public RegisterResult createUser(UserData u) {
        try {
            UserDAO userDAO = new MemoryUserDAO();
            if (getUser(u.username()) == null) {
                userDAO.createUser(u);
            }
            return new RegisterResult("");
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new RegisterResult(message);
        }
    }

    public RegisterResult createAuth() {
        return new RegisterResult("");
    }
}
