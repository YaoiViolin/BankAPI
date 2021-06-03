package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.rs;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CounterParty {
    @JsonProperty("cardNumberFrom")
    private final String cardNumberFrom;

    @JsonProperty("cardNumberTo")
    private final String cardNumberTo;

    @JsonProperty("sum")
    private final BigDecimal sum;

    @ConstructorProperties({"cardNumberFrom", "cardNumberTo", "sum"})
    public CounterParty(String cardNumberFrom, String cardNumberTo, BigDecimal sum) {
        this.cardNumberFrom = cardNumberFrom;
        this.cardNumberTo = cardNumberTo;
        this.sum = sum;
    }

    public String getCardNumberFrom() {
        return cardNumberFrom;
    }

    public String getCardNumberTo() {
        return cardNumberTo;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public static int createTransaction(CounterParty counterParty) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO COUNTERPARTY (CARD_FROM, CARD_TO, SUM) VALUES ( ?, ?, ?)");
            statement.setString(1, counterParty.cardNumberFrom);
            statement.setString(2, counterParty.cardNumberTo);
            statement.setBigDecimal(3, counterParty.sum);
            statement.executeUpdate();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            closeConnection();
            return 0;
        }
    }
    public static List<CounterParty> getAll() {
        List<CounterParty> cpList = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            rs = statement.executeQuery("SELECT * FROM COUNTERPARTY");
            while (rs.next()) {
                String cardNumberFrom = rs.getString("CARD_FROM");
                String cardNumberTo = rs.getString("CARD_TO");
                BigDecimal sum = rs.getBigDecimal("SUM");
                cpList.add(new CounterParty(cardNumberFrom, cardNumberTo, sum));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cpList;
    }
}
