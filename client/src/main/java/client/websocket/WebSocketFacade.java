package client.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import client.ChessArtist;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;

import javax.websocket.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler, ChessBoard board, ChessGame.TeamColor playerColor) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    // Isaih is the best
                    switch (serverMessage.getServerMessageType()) {
                        case ERROR -> {
                            Error error = new Gson().fromJson(message, Error.class);
                            notificationHandler.notify(error);
                        }
                        case NOTIFICATION -> {
                            Notification notification = new Gson().fromJson(message, Notification.class);
                            notificationHandler.notify(notification);
                        }
                        case LOAD_GAME -> {
                            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                            GameData game = loadGame.getGame();

                            PrintStream out = System.out;
                            ChessArtist chessArtist = new ChessArtist(game.game().getBoard());
                            chessArtist.drawChessBoard(out, playerColor);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
        JoinPlayer joinPlayer = new JoinPlayer(authToken, gameID, playerColor);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws ResponseException {
        JoinObserver joinObserver = new JoinObserver(authToken, gameID);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(joinObserver));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        MakeMove makeMove = new MakeMove(authToken, gameID, move);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        Leave leave = new Leave(authToken, gameID);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(leave));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        Resign resign = new Resign(authToken, gameID);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(resign));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
