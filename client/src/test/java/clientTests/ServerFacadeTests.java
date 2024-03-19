package clientTests;

import exception.ResponseException;
import model.GameData;
import model.JoinGameObject;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        facade.clear();
        server.stop();
    }

    @Test
    public void positiveRegisterTest() throws ResponseException {
        facade.clear();
        Assertions.assertNotNull(facade.register("test", "test", "test"));
    }

    @Test
    public void negativeRegisterTest() throws ResponseException {
        facade.clear();
        facade.register("test", "test", "test");
        Assertions.assertThrows(ResponseException.class, () -> facade.register("test", "test", "test"));
    }

    @Test
    public void positiveLoginTest() throws ResponseException {
        facade.clear();
        String authToken = facade.register("test", "test", "test");
        facade.logout(authToken);
        Assertions.assertDoesNotThrow(() -> {
            facade.login("test", "test");
        });
    }

    @Test
    public void negativeLoginTest() throws ResponseException {
        facade.clear();
        Assertions.assertThrows(ResponseException.class, () -> facade.login("test", "test"));
    }

    @Test
    public void positiveCreateGameTest() throws ResponseException {
        facade.clear();
        String authToken = facade.register("test", "test", "test");
        GameData game = new GameData(0, null, null, "test", null);
        Assertions.assertDoesNotThrow(() -> {
            facade.createGame(game, authToken);
        });
    }

    @Test
    public void negativeCreateGameTest() {
        GameData game = new GameData(0, null, null, "test", null);
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(game, "test"));
    }

    @Test
    public void positiveJoinGameTest() throws ResponseException {
        facade.clear();
        String authToken = facade.register("test", "test", "test");
        GameData game = new GameData(1, null, null, "test", null);
        int ID = facade.createGame(game, authToken);
        var joinGameObject = new JoinGameObject(ID, "white");
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinGameObject, authToken));
    }

    @Test
    public void negativeJoinGameTest() {
        var joinGameObject = new JoinGameObject(0, "white");
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(joinGameObject, "test"));
    }

    @Test
    public void clearTest() {
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }

    @Test
    public void positiveListGamesTest() throws ResponseException {
        facade.clear();
        String authToken = facade.register("test", "test", "test");
        GameData game = new GameData(0, null, null, "test", null);
        facade.createGame(game, authToken);
        Assertions.assertDoesNotThrow(() -> {
            facade.listGames(authToken);
        });
    }

    @Test
    public void negativeListGamesTest() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames("test"));
    }

    @Test
    public void positiveObserveGameTest() throws ResponseException {
        facade.clear();
        String authToken = facade.register("test", "test", "test");
        GameData game = new GameData(1, null, null, "test", null);
        int ID = facade.createGame(game, authToken);
        var joinGameObject = new JoinGameObject(ID, null);
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinGameObject, authToken));
    }

    @Test
    public void negativeObserveGameTest() {
        GameData game = new GameData(0, null, null, "test", null);
        Assertions.assertThrows(ResponseException.class, () -> facade.observeGame(game, "test"));
    }
}
