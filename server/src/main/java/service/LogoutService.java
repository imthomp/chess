package service;

import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.AuthDAO;
import model.AuthData;
import result.MessageResult;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService (AuthDAO authDAO) {
        this.authDAO = authDAO;
    }
    public MessageResult logout(String authToken) {
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authToken == null || authData == null) {
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
