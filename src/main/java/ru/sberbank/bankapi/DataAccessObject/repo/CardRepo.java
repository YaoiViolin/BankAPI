package ru.sberbank.bankapi.DataAccessObject.repo;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.con;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.rs;

public interface CardRepo {

    int updateCardBalance(BigDecimal sum);

    static int addCard(Card card, long account_id) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO CARD (NUMBER, ACCOUNT_ID) VALUES ( ?, ?)");
            statement.setString(1, card.getNumber());
            statement.setLong(2, account_id);
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static Card getCard(String number) {
        try {
            Card card = null;
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE NUMBER = ?");
            statement.setString(1, number);
            rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                card = new Card(id, number);
            }
            return card;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }
}
