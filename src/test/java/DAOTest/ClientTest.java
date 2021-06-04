package DAOTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.DataAccessObject.domain.Account;
import ru.sberbank.bankapi.DataAccessObject.domain.Client;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

class ClientTest {

    @BeforeAll
    public static void beforeAll() throws SQLException {
        createConnection();
        dbCreate();
        Statement statement = con.createStatement();
        statement.executeUpdate( "INSERT INTO CLIENT (ID, LOGIN) VALUES (4, 'RICHARD' );");

        statement.executeUpdate("INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ( '0000', 100.1, 4);");
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        closeConnection();
    }

    @Test
    void getClient() {

        Client expectedClient = new Client(4, "RICHARD");
        Client actualClient = Client.getClient("RICHARD");

        Assertions.assertEquals(expectedClient, actualClient);
    }

    @Test
    void getAccounts() {
        Client client = new Client(4, "RICHARD");
        List<Account> actual = client.getAccounts();
        List<Account> expected = new ArrayList<>();
        expected.add(new Account(1, "0000", new BigDecimal("100.10")));

        Assertions.assertEquals(expected, actual);
    }
}