package ru.sberbank.bankapi.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface UserService {
    String addMoneyToCard(String sumString, long cardId, boolean fromJson);

    String withdrawMoneyFromCard(String sumString, long cardId);

    String getBalance(long id);

    String getAllCards(String clientLogin);

    String createNewCard(String accountIdString) throws SQLException;

}
