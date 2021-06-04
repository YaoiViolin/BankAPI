package DBTest;

import org.h2.jdbc.JdbcSQLNonTransientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sberbank.bankapi.DataAccessObject.DBConnector;

import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DBConnectorTest {

    @Test
    void createConnection() {
        DBConnector.createConnection();
        Assertions.assertNotNull(DBConnector.con);
    }

    @Test
    void closeConnection() throws JdbcSQLNonTransientException{
        DBConnector.createConnection();
        DBConnector.closeConnection();

        Throwable thrown = assertThrows(JdbcSQLNonTransientException.class, () -> {
            Statement statement = DBConnector.con.createStatement();
            statement.executeQuery("SELECT  * FROM CLIENT");
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void dbCreateAndInit() {
        DBConnector.createConnection();
        int actualCreate = DBConnector.dbCreate();
        int actualInit = DBConnector.dbInit();
        int expected = 1;

        Assertions.assertEquals(expected, actualCreate);
        Assertions.assertEquals(expected, actualInit);
    }
}