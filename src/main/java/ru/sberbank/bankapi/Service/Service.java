package ru.sberbank.bankapi.Service;

import java.math.BigDecimal;

public interface Service {
    String addMoneyToCard(String sumString, long cardId);

    String getBalance(long id);

    String getAllCards(String clientLogin);

    String createNewCard(String accountIdString);

}
