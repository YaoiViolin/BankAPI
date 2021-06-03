package ru.sberbank.bankapi.DataAccessObject.repo;

import java.math.BigDecimal;

public interface CardRepo {

    int setCardBalance(BigDecimal sum);


}
