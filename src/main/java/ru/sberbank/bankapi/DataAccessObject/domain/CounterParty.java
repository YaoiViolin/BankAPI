package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CounterParty {
    @JsonProperty("cardNumberFrom")
    private final String cardNumberFrom;

    @JsonProperty("cardNumberTo")
    private final String cardNumberTo;

    @JsonProperty("sum")
    private final BigDecimal sum;

    /**
     * Конструктор клсса работы с контагентами
     * @param cardNumberFrom номер карты с которой осуществляется перевод
     * @param cardNumberTo номер карты на которую осуществляется перевод
     * @param sum сумма перевода в рублях
     */
    @ConstructorProperties({"cardNumberFrom", "cardNumberTo", "sum"})
    public CounterParty(String cardNumberFrom, String cardNumberTo, BigDecimal sum) {
        this.cardNumberFrom = cardNumberFrom;
        this.cardNumberTo = cardNumberTo;
        this.sum = sum;
    }

    /**
     * Геттеры
     */
    public String getCardNumberFrom() {
        return cardNumberFrom;
    }

    public String getCardNumberTo() {
        return cardNumberTo;
    }

    public BigDecimal getSum() {
        return sum;
    }

    /**
     * Записывает в БД информацию о переводе с карты на карту
     * @param counterParty экземпляр перевода
     * @return 1 в случае успеха, 0 в случае ошибки
     */
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

    /**
     * @return возвращает информацию обо всех переводах между контрагентами
     */
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

    /**
     * Переопределение методов по умолчанию
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterParty that = (CounterParty) o;
        return getCardNumberFrom().equals(that.getCardNumberFrom()) && getCardNumberTo().equals(that.getCardNumberTo()) && getSum().equals(that.getSum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCardNumberFrom(), getCardNumberTo(), getSum());
    }
}
