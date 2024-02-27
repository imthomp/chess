package service;

import dataAccess.*;
import result.MessageResult;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService (AuthDAO authDAO) {
        this.authDAO = authDAO;
    }
    public MessageResult logout(String authToken) {
        try {
            authDAO.deleteAuth(authToken);
            return new MessageResult(null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new MessageResult(message);
        }
    }
}
