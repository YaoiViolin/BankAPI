package ru.sberbank.bankapi.Service;

import ru.sberbank.bankapi.DataAccessObject.domain.Account;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.Client;
import ru.sberbank.bankapi.DataAccessObject.repo.*;

import java.math.BigDecimal;
import java.util.List;

public interface Service {
    int addMoneyToCard(BigDecimal sum, String number);

    BigDecimal getBalance(String cardNumber);

    String getAllCards(String clientLogin);

    int createNewCard(Account account);

}
