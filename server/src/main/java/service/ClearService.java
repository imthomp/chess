package service;

import chess.ChessPiece;
import chess.ChessPosition;
import dataAccess.*;
import result.ClearResult;

public class ClearService  {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public ClearService (AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public ClearResult clear() {
        try {
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
            return new ClearResult(null);
        } catch (DataAccessException e) {
            String message = e.getMessage();
            return new ClearResult("Error:" + message);
        }
    }
}
