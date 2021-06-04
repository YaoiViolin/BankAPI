package ServiceTest;

import org.junit.jupiter.api.*;
import ru.sberbank.bankapi.DataAccessObject.DBConnector;
import ru.sberbank.bankapi.Service.Interfaces.CounterPartyService;
import ru.sberbank.bankapi.Service.ServiceImpl.CounterPartyServiceImpl;

import java.sql.SQLException;
import java.sql.Statement;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

class CounterPartyServiceImplTest {
    @BeforeAll
    static void beforeAll() throws SQLException {
        createConnection();
        dbCreate();
        Statement statement = DBConnector.con.createStatement();
        statement.executeUpdate( "INSERT INTO CLIENT (ID, LOGIN) VALUES (10, 'RICHARD' ), (11, 'SONYA'), (12, 'ALBERT');");
        statement.executeUpdate("INSERT INTO ACCOUNT (ID,NUMBER, BALANCE, CLIENT_ID) VALUES ( 10,'0000', 100, 10), ( 11,'0001', 200, 11);");
        statement.executeUpdate("INSERT INTO CARD (ID,NUMBER, ACCOUNT_ID) VALUES ( 10,'1111111111111111', 10), ( 11,'2222222222222222', 11);");
    }

    @AfterAll
    static void afterAll() {
        closeConnection();
    }

    @Test
    void makeTransactionsAndGetList() {
        String transJson = "{\"cardNumberFrom\" : \"2222222222222222\", \"cardNumberTo\" : \"1111111111111111\", \"sum\" : 30}\n";
        CounterPartyService service = new CounterPartyServiceImpl();
        String actualBalanceResponse = service.makeTransaction(transJson);
        String expectedBalanceResponse = "[{\"balance\":170.00},{\"balance\":130.00}]";

        Assertions.assertEquals(expectedBalanceResponse, actualBalanceResponse);

        String expectedTransactionsList = "[{\"cardNumberFrom\":\"2222222222222222\",\"cardNumberTo\":\"1111111111111111\",\"sum\":30}]";
        String actualTransactionsList = service.getTransactionsList();
        Assertions.assertEquals(expectedTransactionsList, actualTransactionsList);
    }
}