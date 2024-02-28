package serviceTests;

import dataAccess.exception.DataAccessException;
import dataAccess.object.memory.MemoryAuthDAO;
import dataAccess.object.memory.MemoryUserDAO;
import dataAccess.object.protocol.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;
import result.UserResult;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    UserDAO userDAO = new MemoryUserDAO();

    LoginService service = new LoginService(new MemoryAuthDAO(), userDAO);

    @Test
    public void positiveLoginTest() throws DataAccessException {
        UserData u = new UserData("username", "password", "email");
        userDAO.createUser(u);
        UserResult result = service.login(u);
        assertNull(result.message());
    }

    @Test
    public void negativeLoginTest() {
        UserData u = new UserData("username", "password", "email");
        UserResult result = service.login(u);
        assertEquals(result.message(), "Error: unauthorized");
    }
}
