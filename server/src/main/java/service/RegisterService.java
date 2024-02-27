package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.UserDAO;
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
            // a bad request is any kind of request that can't be completed. logging in with a username that doesn't exist is one
            if () {
                return new UserResult(null, null, "Error: bad request");
            }
            if (userDAO.getUser(u) == null) {
                userDAO.createUser(u);
                String username = u.username();
                String authToken = authDAO.createAuth(username);
                return new UserResult(username, authToken, null);
            } else {
                return new UserResult(null, null, "Error: already taken");
            }
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new UserResult(null, null, "Error:" + message);
        }
    }
}
