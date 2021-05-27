package ru.sberbank.bankapi.DataAccessObject.repo;

import java.util.List;

public interface Account {
    public List<Card> getCardsList();
}
