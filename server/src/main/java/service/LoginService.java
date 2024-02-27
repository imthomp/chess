package service;

import dataAccess.*;
import model.UserData;
import result.UserResult;

public class LoginService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public LoginService (AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public UserResult login(UserData u) {
        try {
            if (userDAO.checkUserCredentials(u)) {
                String username = u.username();
                String authToken = authDAO.createAuth(username);
                return new UserResult(username, authToken, null);
            }
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new UserResult(null, null, "Error:" + message);
        }
        return null;
    }
}
