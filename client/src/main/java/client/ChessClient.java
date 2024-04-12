package client;

import java.io.PrintStream;
import java.util.Arrays;

import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import model.JoinGameObject;
import server.ServerFacade;
import chess.ChessGame;
import chess.ChessBoard;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private State state = State.LOGGED_OUT;
    private String authToken = null;
    private int currentGameID = -1;
    private ChessGame.TeamColor currentPlayerColor = null;

    public ChessClient (String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "move" -> makeMove(params);
                case "leave" -> leave(params);
                case "resign" -> resign(params);
                case "redraw" -> redrawBoard();
                case "highlight" -> highlightLegalMoves();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            authToken = server.register(params[0], params[1], params[2]);
            username = params[0];
            state = State.LOGGED_IN;
            return String.format("You registered as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 1) {
            authToken = server.login(params[0], params[1]);
            username = params[0];
            state = State.LOGGED_IN;
            return String.format("You logged in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();

        ChessGame chessGame = new ChessGame();
        ChessBoard chessBoard = chessGame.getBoard();
        chessBoard.resetBoard();
        if (params.length >= 1) {
            var game = new GameData(0, null, null, params[0], chessGame);
            server.createGame(game, authToken);
            return String.format("You created game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <game name>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();

        var games = server.listGames(authToken);
        if (games == null) {
            return "No games found.";
        } else if (games.length == 0) {
            return "No games found.";
        }
        var sb = new StringBuilder();
        for (var game : games) {
            sb.append(String.format("Game %d: %s, %s vs. %s\n", game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername()));
        }
        return sb.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length >= 1) {
            var id = Integer.parseInt(params[0]);
            var playerColorString = (params.length >= 2) ? params[1] : null;
            var playerColor = (playerColorString != null) ? ChessGame.TeamColor.valueOf(playerColorString.toUpperCase()) : null;
            currentPlayerColor = playerColor;
            var game = getGame(id);
            var joinGameObject = new JoinGameObject(game.gameID(), playerColorString);
            server.joinGame(joinGameObject, authToken);
            state = State.IN_GAME;
            currentGameID = id;

            PrintStream out = System.out;
            ChessBoard board = game.game().getBoard();
            ChessArtist chessArtist = new ChessArtist(board);
            chessArtist.drawChessBoard(out, playerColor);

            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinPlayer(username, id, playerColor);

            return String.format("You joined %s.", game.gameName());
        }
        throw new ResponseException(400, "Expected: <game id> [white|black|<empty>]");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length >= 1) {
            var id = Integer.parseInt(params[0]);
            var game = getGame(id);
            if (game == null) {
                throw new ResponseException(400, "Game not found");
            }
            var newGameData = new GameData(id, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
            server.observeGame(newGameData, authToken);
            state = State.IN_GAME;
            currentGameID = id;

            PrintStream out = System.out;
            ChessBoard board = game.game().getBoard();
            ChessArtist chessArtist = new ChessArtist(board);
            chessArtist.drawChessBoard(out, ChessGame.TeamColor.WHITE);

            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinObserver(username, id);

            return String.format("You are observing %s.", newGameData.gameName());
        }
        throw new ResponseException(400, "Expected: <game id>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout(authToken);
        username = null;
        state = State.LOGGED_OUT;
        return "You logged out.";
    }

    public String makeMove(String... params) throws ResponseException {
        assertSignedIn();
        assertInGame();

        if (params.length >= 3) {
            var id = Integer.parseInt(params[0]);
            var row1 = params[1].charAt(0) - 'a';
            var col1 = Integer.parseInt(params[2]);
            var row2 = params[3].charAt(0) - 'a';
            var col2 = Integer.parseInt(params[4]);
            //var promotion = (params.length >= 6) ? params[5] : null;
            var game = getGame(id);
            if (game == null) {
                throw new ResponseException(400, "Game not found");
            }
            var move = new ChessMove(new ChessPosition(row1, col1), new ChessPosition(row2, col2), null);
            ws.makeMove(authToken, id, move);
            return String.format("You made a move in %s.", game.gameName());
        }
        throw new ResponseException(400, "Expected: <game id> <x1> <y1> <x2> <y2>");
    }

    public String leave(String... params) throws ResponseException {
        assertSignedIn();
        assertInGame();

        if (params.length >= 1) {
            var id = Integer.parseInt(params[0]);
            var game = getGame(id);
            if (game == null) {
                throw new ResponseException(400, "Game not found");
            }
            ws.leave(authToken, id);
            state = State.LOGGED_IN;
            currentGameID = -1;

            return String.format("You left %s.", game.gameName());
        }
        throw new ResponseException(400, "Expected: <game id>");
    }

    public String resign(String... params) throws ResponseException {
        assertSignedIn();
        assertInGame();

        if (params.length >= 1) {
            var id = Integer.parseInt(params[0]);
            var game = getGame(id);
            if (game == null) {
                throw new ResponseException(400, "Game not found");
            }
            ws.resign(authToken, id);
            return String.format("You resigned from %s.", game.gameName());
        }
        throw new ResponseException(400, "Expected: <game id>");
    }

    public String redrawBoard() throws ResponseException {
        PrintStream out = System.out;
        var game = getGame(currentGameID);
        ChessBoard board = game.game().getBoard();
        ChessArtist chessArtist = new ChessArtist(board);
        chessArtist.drawChessBoard(out, currentPlayerColor);
        return "";
    }

    public String highlightLegalMoves() throws ResponseException {
        PrintStream out = System.out;
        var game = getGame(currentGameID);
        ChessBoard board = game.game().getBoard();
        ChessArtist chessArtist = new ChessArtist(board);
        chessArtist.highlightLegalMoves(out, currentPlayerColor, null);
        return "";
    }

    private GameData getGame(int id) throws ResponseException {
        for (var game : server.listGames(authToken)) {
            if (game.gameID() == id) {
                return game;
            }
        }
        return null;
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - register an account
                    login <USERNAME> <PASSWORD> - login to chess
                    quit - quit playing chess
                    help - help with possible commands""";
        }
        if (state == State.IN_GAME) {
            return """
                    move <ID> <row1> <col1> <row2> <col2> - to make a move
                    leave <ID> - to leave a game
                    resign <ID> - to resign from a game
                    redraw - to redraw the board
                    highlight - to highlight legal moves
                    quit - playing chess
                    help - with possible commands""";
        }
        return """
                create <NAME> - create a game
                list - list games
                join <ID> [white|black|<empty>] - join a game
                observe <ID> - observe a game
                logout - logout of chess
                quit - quit playing chess
                help - help with possible commands""";
    }

    private void assertSignedIn() throws ResponseException {
        if (authToken == null) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    private void assertInGame() throws ResponseException {
        if (authToken == null || ws == null) { // to be fixed
            throw new ResponseException(400, "You must sign in");
        }
    }
}