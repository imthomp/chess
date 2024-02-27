package service;

import dataAccess.*;
import model.UserData;
import result.RegisterResult;

public class LoginService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public LoginService (AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult login(UserData u) {
        try {
            if (userDAO.checkUserCredentials(u)) {
                String username = u.username();
                String authToken = authDAO.createAuth(username);
                return new RegisterResult(username, authToken, null);
            }
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new RegisterResult(null, null, "Error:" + message);
        }
        return null;
    }
}
