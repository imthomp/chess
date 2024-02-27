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
            if (u.username() == null || u.password() == null) {
                throw new DataAccessException("bad request");
            }
            if (userDAO.getUser(u) == null) {
                userDAO.createUser(u);
                String username = u.username();
                String authToken = authDAO.createAuth(username);
                return new UserResult(username, authToken, null);
            } else {
                throw new DataAccessException("already taken");
            }
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new UserResult(null, null, "Error: " + message);
        }
    }
}
