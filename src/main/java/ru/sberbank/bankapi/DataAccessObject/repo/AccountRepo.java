package ru.sberbank.bankapi.DataAccessObject.repo;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.util.List;

/**
 * Интерфейс счёта клиента
 */
public interface AccountRepo {
    public List<Card> getCards();
}
