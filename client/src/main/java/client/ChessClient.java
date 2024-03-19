package client;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    String serverUrl;
    private State state = State.LOGGED_OUT;
    Repl repl;
    private String authToken = null;

    public ChessClient (String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
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
        if (params.length >= 1) {
            var game = new GameData(0, null, null, params[0], null);
            server.createGame(game, authToken);
            return String.format("You created game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <game name>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(authToken);
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            // server takes in String authToken, String playerColor, Integer gameID
            var id = Integer.parseInt(params[0]);
            var game = getGame(id);
            if (game == null) {
                throw new ResponseException(400, "Game not found");
            }
            var playerColor = (params.length >= 2) ? params[1] : "";
            if (playerColor == null) {
                var newGameData = new GameData(id, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                server.joinGame(newGameData, authToken);
            } else if (Objects.equals(playerColor, "white")) {
                if (game.whiteUsername() != null) {
                    throw new ResponseException(400, "White already taken");
                }
                var newGameData = new GameData(id, username, game.blackUsername(), game.gameName(), game.game());
                server.joinGame(newGameData, authToken);
            } else if (Objects.equals(playerColor, "BLACK")) {
                if (game.blackUsername() != null) {
                    throw new ResponseException(400, "Black already taken");
                }
                var newGameData = new GameData(id, game.whiteUsername(), username, game.gameName(), game.game());
                server.joinGame(newGameData, authToken);
            }
        }
        throw new ResponseException(400, "Expected: <game id> [WHITE|BLACK|<empty>]");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var id = Integer.parseInt(params[0]);
            var game = getGame(id);
            if (game == null) {
                throw new ResponseException(400, "Game not found");
            }
            PrintStream out = System.out;
            ChessArtist.drawBoardPerspectives(out, game.game().getBoard());
            return String.format("You are observing game %d", id);
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
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<empty>]
                observe <ID>
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (authToken == null) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}