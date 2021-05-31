package ru.sberbank.bankapi.DataAccessObject.repo;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;

import java.util.List;

public interface AccountRepo {
    public List<Card> getCards();
}
