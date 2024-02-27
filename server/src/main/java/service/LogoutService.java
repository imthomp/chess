package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import result.MessageResult;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService (AuthDAO authDAO) {
        this.authDAO = authDAO;
    }
    public MessageResult logout(String authToken) {
        try {
            if (authToken == null) {
                throw new DataAccessException("unauthorized");
            }
            if (authDAO.getAuth(authToken) == null) {
                throw new DataAccessException("unauthorized");
            }
            authDAO.deleteAuth(authToken);
            return new MessageResult(null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new MessageResult("Error: " + message);
        }
    }
}
