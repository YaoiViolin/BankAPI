package ru.sberbank.bankapi.Service;

import ru.sberbank.bankapi.DataAccessObject.repo.Account;
import ru.sberbank.bankapi.DataAccessObject.repo.Card;

import java.util.List;

public class ServiceImpl implements Service{
    @Override
    public int addMoneyToCard(int sum, Card card) {
        return 0;
    }

    @Override
    public int getBalance(Card card) {
        return 0;
    }

    @Override
    public List<Card> getAllCards(Account account) {
        return null;
    }

    @Override
    public int createNewCard(Account account) {
        return 0;
    }
}
