package ru.sberbank.bankapi.DataAccessObject.repo;

import java.math.BigDecimal;

/**
 * Интерфейс карты клиента
 */
public interface CardRepo {

    int setCardBalance(BigDecimal sum);


}
