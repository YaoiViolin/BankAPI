package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public Card(int id, String number) {
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
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ACCOUNT WHERE ACCOUNT.ID = (SELECT ACCOUNT_ID FROM CARD WHERE CARD.NUMBER = ?)");
            statement.setString(1,this.number);
            BigDecimal BD = null;
            rs = statement.executeQuery();

            while (rs.next()) {
                BD = rs.getBigDecimal("BALANCE");
                System.out.println(BD);
            }
            return BD;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateCardBalance(BigDecimal sum) {
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNT.ID = (SELECT ACCOUNT_ID FROM CARD WHERE CARD.NUMBER = ?)");
            statement.setBigDecimal(1, sum);
            statement.setString(2,this.number);
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }



    public static String getLastCardNumber() {
        try {
            String number = null;
            Statement statement = con.createStatement();
            rs = statement.executeQuery("SELECT TOP 1 NUMBER FROM CARD ORDER BY NUMBER DESC");
            while (rs.next()) {
                number = rs.getString("NUMBER");
            }
            return number;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
