package DAOTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.DataAccessObject.domain.CounterParty;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

class CounterPartyTest {

    @BeforeAll
    public static void beforeAll() throws SQLException {
        createConnection();
        dbCreate();
    }

    @Test
    void createTransactionAndGetList() {
        CounterParty cp = new CounterParty("1111111111111111", "222222222222222", new BigDecimal("150.00"));
        CounterParty.createTransaction(cp);
        List<CounterParty> listActual = CounterParty.getAll();
        List<CounterParty> expected = new ArrayList<>();
        expected.add(cp);

        Assertions.assertEquals(expected, listActual);
    }
}