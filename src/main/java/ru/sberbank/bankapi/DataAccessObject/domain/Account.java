package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ru.sberbank.bankapi.DataAccessObject.repo.AccountRepo;
import ru.sberbank.bankapi.DataAccessObject.repo.CardRepo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

@JsonAutoDetect
public class Account implements AccountRepo {
    private long id;
    private String number;
    private BigDecimal balance;
    private List<Card> cards;

    public Account(int id, String number, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public List<Card> getCards() {
        cards = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE ACCOUNT_ID = ?");
            statement.setLong(1, this.id);
            rs = statement.executeQuery();
            while (rs.next()) {
                int cardId = rs.getInt("ID");
                String cardNumber = rs.getString("NUMBER");
                cards.add(new Card(cardId, cardNumber));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cards;
    }

    @Override
    public String toString() {
        return "AccountImpl{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", cards=" + cards +
                '}';
    }
}
