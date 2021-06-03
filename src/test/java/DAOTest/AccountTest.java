package DAOTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.DataAccessObject.domain.Account;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.closeConnection;

class AccountTest {
    static long id;
    @BeforeAll
    public static void beforeAll() throws SQLException {
        createConnection();
        dbInit();
        Statement statement = con.createStatement();
        statement.executeUpdate("INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ( '0000', 100.1, 3 );");
        statement.executeUpdate("INSERT INTO CARD (NUMBER, ACCOUNT_ID) VALUES ( '1000000000000000'," +
                "(SELECT ID FROM ACCOUNT WHERE ACCOUNT.NUMBER = '0000'));");
        statement.executeUpdate("INSERT INTO CARD (NUMBER, ACCOUNT_ID) VALUES ( '2000000000000000'," +
                "(SELECT ID FROM ACCOUNT WHERE ACCOUNT.NUMBER = '0000'));");

        rs = statement.executeQuery("SELECT ID FROM ACCOUNT WHERE ACCOUNT.NUMBER = '0000'");
        while (rs.next()) {
            id = rs.getLong("ID");
        }
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        closeConnection();
    }

    @Test
    void getAccountAndCardsList() {

        Account accountExpected = new Account(id, "0000", new BigDecimal("100.10"));
        Account accountActual = Account.getAccount("0000");

        Assertions.assertEquals(accountExpected, accountActual);

        List<Card> expected = new ArrayList<>();
        expected.add(new Card(0, "1000000000000000"));
        expected.add(new Card(1, "2000000000000000"));

        List<Card> actual = accountActual.getCards();

        Assertions.assertEquals(expected, actual);

    }
}