package ru.sberbank.bankapi.DataAccessObject.domain;

import ru.sberbank.bankapi.DataAccessObject.DBConnector;
import ru.sberbank.bankapi.DataAccessObject.repo.Account;
import ru.sberbank.bankapi.DataAccessObject.repo.Card;
import ru.sberbank.bankapi.DataAccessObject.repo.Client;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

public class AccountImpl implements Account {
    private int id;
    private String number;
    private BigDecimal balance;
    private Client client;
    private List<Card> cards;

    public AccountImpl(int id, String number, BigDecimal balance, Client client) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public List<Card> getCardsList() {
        cards = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE ACCOUNT_ID = ?");
            statement.setInt(1, this.id);
            rs = statement.executeQuery();
            while (rs.next()) {
                int cardId = rs.getInt("ID");
                String cardNumber = rs.getString("NUMBER");
                BigDecimal balance = rs.getBigDecimal("BALANCE");
                cards.add(new CardImpl(cardId, cardNumber, balance, this));
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
                ", client=" + client +
                ", cards=" + cards +
                '}';
    }
}
