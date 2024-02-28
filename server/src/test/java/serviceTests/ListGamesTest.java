package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.memory.MemoryAuthDAO;
import dataAccess.object.memory.MemoryGameDAO;
import dataAccess.object.protocol.AuthDAO;
import dataAccess.object.protocol.GameDAO;
import org.junit.jupiter.api.Test;
import result.ListGamesResult;
import service.ListGamesService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ListGamesTest {
    AuthDAO authDAO = new MemoryAuthDAO();

    ListGamesService service = new ListGamesService(authDAO, new MemoryGameDAO());

    @Test
    public void positiveListGamesTest() throws DataAccessException {
        String authToken = authDAO.createAuth("username");
        ListGamesResult result = service.listGames(authToken);
        assertNull(result.message());
    }

    @Test
    public void negativeListGamesTest() {
        ListGamesResult result = service.listGames("badAuthToken");
        assertEquals(result.message(), "Error: unauthorized");
    }
}
