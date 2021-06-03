package DAOTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

class CardTest {

    @BeforeAll
    public static void beforeAll() throws SQLException {
        createConnection();
        dbInit();
        Statement statement = con.createStatement();
        statement.executeUpdate("INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ( '0000', 100.10, 3 );");
        statement.executeUpdate("INSERT INTO CARD (NUMBER, ACCOUNT_ID) VALUES ( '1000000000000000'," +
                "(SELECT ID FROM ACCOUNT WHERE ACCOUNT.NUMBER = '0000'));");
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        closeConnection();
    }

    @Test
    void getBalance() {
        Card card = new Card(1, "1000000000000000");
        BigDecimal balanceActual = card.getBalance();
        BigDecimal balanceExpected = new BigDecimal(100.1).setScale(2, RoundingMode.HALF_UP);

        Assertions.assertEquals(balanceExpected, balanceActual);

    }

    @Test
    void setCardBalance() {
        Card card = new Card(1,"1000000000000000");

        card.setCardBalance(new BigDecimal("50.1"));
        BigDecimal balanceActual = card.getBalance();
        BigDecimal balanceExpected = new BigDecimal("50.10").setScale(2, RoundingMode.HALF_UP);

        Assertions.assertEquals(balanceExpected, balanceActual);
    }

    @Test
    void getLastCardNumber() {
        String expected = "1000000000000000";
        String actual = Card.getLastCardNumber();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addAndThenGetCard() {
        Card cardExpected = new Card(1, "1234123412341234");
        int expected = 1;
        int actual = Card.addCard(cardExpected, 1);

        Card cardActual =  Card.getCard("1234123412341234");

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(cardExpected.getNumber(), cardActual.getNumber());
    }

}