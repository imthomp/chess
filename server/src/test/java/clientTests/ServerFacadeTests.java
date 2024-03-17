package clientTests;

import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void positiveRegisterTest() throws ResponseException {
        facade.register("test", "test", "test");
    }

    @Test
    public void negativeRegisterTest() throws ResponseException {
        facade.register("test", "test", "test");
    }

    @Test
    public void positiveLoginTest() throws ResponseException {
        facade.login("test", "test");
    }

    @Test
    public void negativeLoginTest() throws ResponseException {
        facade.login("test", "test");
    }

    @Test
    public void positiveCreateGameTest() throws ResponseException {
        GameData game = new GameData(0, null, null, "test", null);
        facade.createGame(game);
    }

    @Test
    public void negativeCreateGameTest() throws ResponseException {
        GameData game = new GameData(0, null, null, "test", null);
        facade.createGame(game);
    }

    @Test
    public void positiveJoinGameTest() throws ResponseException {
        GameData game = new GameData(0, null, null, "test", null);
        facade.joinGame(game);
    }

    @Test
    public void negativeJoinGameTest() throws ResponseException {
        GameData game = new GameData(0, null, null, "test", null);
        facade.joinGame(game);
    }

    @Test
    public void clearTest() throws ResponseException {
        facade.clear();
    }

    @Test
    public void positiveListGamesTest() throws ResponseException {
        facade.listGames();
    }

    @Test
    public void negativeListGamesTest() throws ResponseException {
        facade.listGames();
    }

}
