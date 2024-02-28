package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.memory.MemoryAuthDAO;
import dataAccess.object.protocol.AuthDAO;
import org.junit.jupiter.api.Test;
import result.MessageResult;
import service.LogoutService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LogoutTest {
    AuthDAO authDAO = new MemoryAuthDAO();

    LogoutService service = new LogoutService(authDAO);

    @Test
    public void positiveLogoutTest() throws DataAccessException {
        String authToken = authDAO.createAuth("username");
        MessageResult result = service.logout(authToken);
        assertNull(result.message());
    }

    @Test
    public void negativeLogoutTest() {
        MessageResult result = service.logout("badAuthToken");
        assertEquals(result.message(), "Error: unauthorized");
    }
}
