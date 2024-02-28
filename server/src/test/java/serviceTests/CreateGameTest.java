package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.memory.MemoryAuthDAO;
import dataAccess.object.memory.MemoryGameDAO;
import dataAccess.object.protocol.AuthDAO;
import org.junit.jupiter.api.Test;
import result.GameResult;
import service.CreateGameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateGameTest {
    AuthDAO authDAO = new MemoryAuthDAO();

    CreateGameService service = new CreateGameService(authDAO, new MemoryGameDAO());

    @Test
    public void positiveCreateGameTest() throws DataAccessException {
        String authToken = authDAO.createAuth("username");
        GameResult result = service.createGame(authToken, "gameName");
        assertNull(result.message());
    }

    @Test
    public void negativeCreateGameTest() {
        GameResult result = service.createGame("badAuthToken", "gameName");
        assertEquals(result.message(), "Error: unauthorized");
    }
}
