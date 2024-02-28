package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.memory.MemoryAuthDAO;
import dataAccess.object.memory.MemoryGameDAO;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import org.junit.jupiter.api.Test;
import result.MessageResult;
import service.JoinGameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JoinGameTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    JoinGameService service = new JoinGameService(authDAO, gameDAO);

    @Test
    public void positiveJoinGameTest() throws DataAccessException {
        String authToken = authDAO.createAuth("username");
        gameDAO.createGame(1, "gameName");
        MessageResult result = service.joinGame(authToken, "WHITE", 1);
        assertNull(result.message());
    }

    @Test
    public void negativeJoinGameTest() throws DataAccessException {
        String authToken = authDAO.createAuth("username");
        MessageResult result = service.joinGame(authToken, "WHITE", 1);
        assertEquals(result.message(), "Error: bad request");
    }
}
