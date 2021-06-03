package ServiceTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.Service.InterFaces.UserService;
import ru.sberbank.bankapi.Service.UserServiceImpl;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

class UserServiceImplTest {

    @BeforeAll
    static void beforeAll() throws SQLException {
        createConnection();
        dbInit();
        Statement statement = con.createStatement();
        statement.executeUpdate( "INSERT INTO CLIENT (ID, LOGIN) VALUES (10, 'RICHARD' ), (11, 'SONYA'), (12, 'ALBERT');");
        statement.executeUpdate("INSERT INTO ACCOUNT (ID,NUMBER, BALANCE, CLIENT_ID) VALUES ( 10,'0000', 100, 10), ( 11,'0001', 200, 11);");
        statement.executeUpdate("INSERT INTO CARD (ID,NUMBER, ACCOUNT_ID) VALUES ( 10,'0000111100001111', 10), ( 11,'1111111100001111', 11);");
    }

    @AfterAll
    static void afterAll() {
        closeConnection();
    }

    @Test
    void addMoneyToCard() {
        String jsonString = "{\"sum\" : 50}";
        UserService service = new UserServiceImpl();
        String actualResponse = service.addMoneyToCard(jsonString, 10, true);
        String expectedResponse = "{\"balance\":150.00}";
        String negativeResponse =  service.addMoneyToCard(jsonString, 15, true);

        Assertions.assertEquals(expectedResponse, actualResponse);
        assertNull(negativeResponse);
    }

    @Test
    void getBalance() {
        String jsonString = "{\"id\" : 11}";
        UserService service = new UserServiceImpl();
        String actualResponse = service.getBalance(11);
        String expectedResponse = "{\"balance\":200.00}";
        String negativeResponse = service.getBalance(15);

        Assertions.assertEquals(expectedResponse, actualResponse);
        assertNull(negativeResponse);

    }

    @Test
    void getAllCards() {
        UserService service = new UserServiceImpl();
        String actualResponse = service.getAllCards("SONYA");
        String expectedResponse = "{\"id\":11,\"number\":\"1111111100001111\"}";
        String negativeResponse = service.getAllCards("ALBERT");

        Assertions.assertEquals(expectedResponse, actualResponse);
        assertNull(negativeResponse);
    }

    @Test
    void createNewCard() throws SQLException {

        UserService service = new UserServiceImpl();
        String actualResponse = service.createNewCard("{\"id\" : 10}");
        String expectedResponse = "{\"id\":12,\"number\":\"1111111100001112\"}";
        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}