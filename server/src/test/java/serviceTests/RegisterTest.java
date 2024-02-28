package serviceTests;

import dataAccess.object.memory.MemoryAuthDAO;
import dataAccess.object.memory.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;
import result.UserResult;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    RegisterService service = new RegisterService(new MemoryAuthDAO(), new MemoryUserDAO());

    @Test
    public void positiveRegisterTest() {
        UserData u = new UserData("username", "password", "email");
        UserResult result = service.register(u);
        assertNull(result.message());
    }

    @Test
    public void negativeRegisterTest() {
        UserData nullUser = new UserData(null, null, null);
        UserResult result = service.register(nullUser);
        assertEquals(result.message(), "Error: bad request");
    }
}
