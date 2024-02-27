package service;

import dataAccess.*;
import model.UserData;
import result.UserResult;

public class RegisterService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public RegisterService (AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public UserResult register(UserData u) {
        try {
            if (userDAO.getUser(u) == null) {
                userDAO.createUser(u);
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
