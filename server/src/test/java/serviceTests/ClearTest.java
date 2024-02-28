package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.memory.*;
import dataAccess.object.protocol.AuthDAO;
import org.junit.jupiter.api.Test;
import result.MessageResult;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearTest {
    AuthDAO authDAO = new MemoryAuthDAO();
    ClearService service = new ClearService(authDAO, new MemoryGameDAO(), new MemoryUserDAO());

    @Test
    public void clearTest() throws DataAccessException {
        authDAO.createAuth("username");
        MessageResult result = service.clear();
        assertNull(result.message());
    }
}
