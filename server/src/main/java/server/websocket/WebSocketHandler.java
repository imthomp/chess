package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.object.sql.SQLAuthDAO;
import dataAccess.object.sql.SQLGameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(action.getAuthString(), session, message);
            case JOIN_OBSERVER -> joinObserver(action.getAuthString(), session, message);
            case LEAVE -> leave(action.getAuthString(), message);
            case MAKE_MOVE -> makeMove(action.getAuthString(), message);
            case RESIGN -> resign(action.getAuthString(), message);
        }
    }

    private void joinPlayer(String authToken, Session session, String message) {
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
            int gameID = joinPlayer.getGameID();
            GameData game = new SQLGameDAO().getGame(gameID);
            AuthData auth = new SQLAuthDAO().getAuth(authToken);

            if (isInvalidAuth(authToken)) {
                var error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            String username = auth.username();
            ChessGame.TeamColor playerColor = joinPlayer.getPlayerColor();

            if (isInvalidJoin(username, playerColor, game, auth)) {
                var error = new Error("Invalid join request");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            String notificationMessage = username + " has joined the game as the " + playerColor + " player!";
            Notification notification = new Notification(notificationMessage);
            LoadGame loadGame = new LoadGame(game);

            connections.add(authToken, session, gameID);
            connections.notifyUser(authToken, loadGame, gameID);
            connections.notifyOthers(authToken, notification, gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void joinObserver(String authToken, Session session, String message) {
        try {
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
            int gameID = joinObserver.getGameID();
            GameData game = new SQLGameDAO().getGame(gameID);
            AuthData auth = new SQLAuthDAO().getAuth(authToken);

            if (isInvalidAuth(authToken)) {
                var error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            String username = auth.username();

            if (isInvalidJoin(username, null, game, auth)) {
                var error = new Error("Invalid join request");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            String notificationMessage = username + " has joined the game!";
            Notification notification = new Notification(notificationMessage);
            LoadGame loadGame = new LoadGame(game);

            connections.add(authToken, session, gameID);
            connections.notifyUser(authToken, loadGame, gameID);
            connections.notifyOthers(authToken, notification, gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void leave(String authToken, String message) {
        try {
            Leave leave = new Gson().fromJson(message, Leave.class);
            int gameID = leave.getGameID();
            GameData game = new SQLGameDAO().getGame(gameID);
            AuthData auth = new SQLAuthDAO().getAuth(authToken);
            String username = auth.username();

            new SQLGameDAO().updateGame(game);

            String notificationMessage = username + " has left the game!";
            Notification notification = new Notification(notificationMessage);

            connections.remove(authToken, gameID);
            connections.notifyOthers(authToken, notification, gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void makeMove(String authToken, String message) {
        try {
            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
            int gameID = makeMove.getGameID();
            GameData game = new SQLGameDAO().getGame(gameID);
            AuthData auth = new SQLAuthDAO().getAuth(authToken);
            String username = auth.username();
            ChessMove move = makeMove.getMove();
            ChessGame chessGame = game.game();

            ChessGame.TeamColor playerColor = null;
            ChessGame.TeamColor opponentColor = null;
            if (Objects.equals(game.whiteUsername(), username)) {
                playerColor = ChessGame.TeamColor.WHITE;
                opponentColor = ChessGame.TeamColor.BLACK;
            } else if (Objects.equals(game.blackUsername(), username)) {
                playerColor = ChessGame.TeamColor.BLACK;
                opponentColor = ChessGame.TeamColor.WHITE;
            }

            if (chessGame.gameIsDone) {
                var error = new Error("Game is done");
                connections.notifyUser(authToken, error, gameID);
                return;
            }

            var validMoves = chessGame.validMoves(move.getStartPosition());
            if (!validMoves.contains(move)) {
                var error = new Error("Invalid move");
                connections.notifyUser(authToken, error, gameID);
                return;
            }

            if (chessGame.getTeamTurn() != playerColor) {
                var error = new Error("Not your turn");
                connections.notifyUser(authToken, error, gameID);
                return;
            }

            try {
                chessGame.makeMove(move);
            } catch (InvalidMoveException e) {
                var error = new Error("Invalid move");
                connections.notifyUser(authToken, error, gameID);
                return;
            }

            new SQLGameDAO().updateGame(game);

            String notificationMessage = username + " has made a move!";
            Notification notification = new Notification(notificationMessage);
            connections.notifyOthers(authToken, notification, gameID);

            if (chessGame.isInCheck(opponentColor)) {
                if (chessGame.isInCheckmate(opponentColor)) {
                    String checkmateMessage = username + " has checkmated " + opponentColor + "!";
                    Notification checkmateNotification = new Notification(checkmateMessage);
                    connections.notifyAll(checkmateNotification, gameID);
                } else {
                    String checkMessage = username + " has put " + opponentColor + " in check!";
                    Notification checkNotification = new Notification(checkMessage);
                    connections.notifyAll(checkNotification, gameID);
                }
            }

            connections.notifyAll(new LoadGame(game), gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void resign(String authToken, String message) {
        try {
            Resign resign = new Gson().fromJson(message, Resign.class);
            int gameID = resign.getGameID();
            GameData game = new SQLGameDAO().getGame(gameID);
            AuthData auth = new SQLAuthDAO().getAuth(authToken);
            String username = auth.username();

            ChessGame chessGame = game.game();

            if (chessGame.gameIsDone) {
                var error = new Error("Game is done");
                connections.notifyUser(authToken, error, gameID);
                return;
            }

            if (!Objects.equals(username, game.whiteUsername()) && !Objects.equals(username, game.blackUsername())) {
                var error = new Error("Invalid resign request");
                connections.notifyUser(authToken, error, gameID);
                return;
            }

            chessGame.resign();

            new SQLGameDAO().updateGame(game);

            String notificationMessage = username + " has resigned!";
            Notification notification = new Notification(notificationMessage);

            connections.notifyAll(notification, gameID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isInvalidJoin(String username, ChessGame.TeamColor playerColor, GameData gameData, AuthData authData) {
        if (authData == null) {
            return true;
        }
        if (gameData == null) {
            return true;
        }
        if (playerColor == null) {
            return false;
        }
        if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
            return !Objects.equals(gameData.whiteUsername(), username);
        }
        if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
            return !Objects.equals(gameData.blackUsername(), username);
        }
        return true;
    }

    private boolean isInvalidAuth(String authToken) {
        try {
            return new SQLAuthDAO().getAuth(authToken) == null;
        } catch (Exception e) {
            return true;
        }
    }
}