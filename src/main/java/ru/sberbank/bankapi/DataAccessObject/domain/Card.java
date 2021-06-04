package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.sberbank.bankapi.DataAccessObject.repo.CardRepo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.*;


@JsonAutoDetect
public class Card implements CardRepo {
    private final long id;
    private final String number;

    /**
     * Констуктор класса "карта клиента"
     * @param id уникальный идентификатор
     * @param number уникальный 16-значный номер
     */
    public Card(long id, String number) {
        this.id = id;
        this.number = number;
    }

    /**
     * Геттеры и сеттеры
     */
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    /**
     * Возвращает баланс на счёте, к которому привязана карта
     * @return баланс в рублях
     */
    @JsonIgnore
    public BigDecimal getBalance() {
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ACCOUNT WHERE ACCOUNT.ID = (SELECT ACCOUNT_ID FROM CARD WHERE CARD.NUMBER = ?)");
            statement.setString(1,this.number);
            BigDecimal BD = null;
            rs = statement.executeQuery();

            while (rs.next()) {
                BD = rs.getBigDecimal("BALANCE");
            }
            return BD;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Устанавливает баланс счёта, к которому привязана эта карта
     * @param sum значение баланса, которое должно быть установлено
     * @return 1 в случае успеха, 0 в случае ошибки
     */
    @Override
    public int setCardBalance(BigDecimal sum) {
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNT.ID = (SELECT ACCOUNT_ID FROM CARD WHERE CARD.NUMBER = ?)");
            statement.setBigDecimal(1, sum);
            statement.setString(2,this.number);
            statement.executeUpdate();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    /**
     * Отвечает за уникальность номеров карт
     * @return последний номер карты из базы
     */
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

    /**
     * Добавляет карту в базу
     * @param card карта, который нужно добавить в базу
     * @param account_id номер счёта, к которому нужно привязать карту
     * @return 1 в случае успешного добавления, 0 в случае ошибки
     */
    public static int addCard(Card card, long account_id) {
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO CARD (NUMBER, ACCOUNT_ID) VALUES ( ?, ?)");
            statement.setString(1, card.getNumber());
            statement.setLong(2, account_id);
            statement.executeUpdate();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            closeConnection();
            return 0;
        }
    }

    /**
     * Достает из базы карту по id
     * @param id карты, которую треубется достать из базы
     * @return экзамепляр класса карты
     */
    public static Card getCard(Long id) {
        try {
            Card card = null;
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE ID = ?");
            statement.setLong(1, id);
            rs = statement.executeQuery();
            while (rs.next()) {
                String number = rs.getString("NUMBER");
                card = new Card(id, number);
            }
            return card;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            closeConnection();
            return null;
        }

    }
    /**
     * Достает из базы карту по номеру
     * @param number карты, которую треубется достать из базы
     * @return экзамепляр класса карты
     */
    public static Card getCard(String number) {
        try {
            Card card = null;
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CARD WHERE NUMBER = ?");
            statement.setString(1, number);
            rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("ID");
                card = new Card(id, number);
            }
            return card;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            closeConnection();
            return null;
        }

    }

    /**
     * Переопределение методов по умолчанию
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getNumber().equals(card.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }

    @Override
    public String toString() {
        return "CardImpl{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
