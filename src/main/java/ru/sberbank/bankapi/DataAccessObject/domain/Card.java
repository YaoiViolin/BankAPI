package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.sberbank.bankapi.DataAccessObject.DBConnector;
import ru.sberbank.bankapi.DataAccessObject.repo.CardRepo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.con;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.rs;

@JsonAutoDetect
public class Card implements CardRepo {
    private final long id;
    private final String number;

    public Card(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    @JsonIgnore
    public BigDecimal getBalance() {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ACCOUNT WHERE ACCOUNT.ID = (SELECT ACCOUNT_ID FROM CARD WHERE CARD.NUMBER = ?)");
            statement.setString(1,this.number);
            BigDecimal BD = null;
            rs = statement.executeQuery();

            while (rs.next()) {
                BD = rs.getBigDecimal("BALANCE");
                System.out.println(BD);
            }
            connector.closeConnection();
            return BD;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connector.closeConnection();
            return null;
        }
    }

    @Override
    public int updateCardBalance(BigDecimal sum) {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNT.ID = (SELECT ACCOUNT_ID FROM CARD WHERE CARD.NUMBER = ?)");
            statement.setBigDecimal(1, sum);
            statement.setString(2,this.number);
            statement.executeUpdate();
            connector.closeConnection();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connector.closeConnection();
            return 0;
        }
    }

    public static String getLastCardNumber() {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        try {
            String number = null;
            Statement statement = con.createStatement();
            rs = statement.executeQuery("SELECT TOP 1 NUMBER FROM CARD ORDER BY NUMBER DESC");
            while (rs.next()) {
                number = rs.getString("NUMBER");
            }
            connector.closeConnection();
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connector.closeConnection();
            return null;
        }
    }

    public static int addCard(Card card, long account_id) {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO CARD (NUMBER, ACCOUNT_ID) VALUES ( ?, ?)");
            statement.setString(1, card.getNumber());
            statement.setLong(2, account_id);
            statement.executeUpdate();
            connector.closeConnection();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connector.closeConnection();
            return 0;
        }
    }

    public static Card getCard(Long id) {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        try {
            Card card = null;
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE ID = ?");
            statement.setLong(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                String number = rs.getString("NUMBER");
                card = new Card(id, number);
            }
            connector.closeConnection();
            return card;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connector.closeConnection();
            return null;
        }

    }

    public static Card getCard(String number) {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        try {
            Card card = null;
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE NUMBER = ?");
            statement.setString(1, number);
            rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("NUMBER");
                card = new Card(id, number);
            }
            connector.closeConnection();
            return card;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            connector.closeConnection();
            return null;
        }

    }


    @Override
    public String toString() {
        return "CardImpl{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
