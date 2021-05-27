package ru.sberbank.bankapi.Service;

import ru.sberbank.bankapi.DataAccessObject.repo.*;

import java.util.List;

public interface Service {
    public int addMoneyToCard(int sum, Card card);

    public int getBalance(Card card);

    public List<Card> getAllCards(Account account);

    public int createNewCard(Account account);

}
