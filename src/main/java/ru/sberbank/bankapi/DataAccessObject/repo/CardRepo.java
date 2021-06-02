package ru.sberbank.bankapi.DataAccessObject.repo;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.con;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.rs;

public interface CardRepo {

    int setCardBalance(BigDecimal sum);


}
