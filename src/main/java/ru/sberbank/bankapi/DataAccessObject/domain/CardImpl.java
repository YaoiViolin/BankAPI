package ru.sberbank.bankapi.DataAccessObject.domain;

import ru.sberbank.bankapi.DataAccessObject.repo.Account;
import ru.sberbank.bankapi.DataAccessObject.repo.Card;

import java.math.BigDecimal;

public class CardImpl implements Card {
    private int id;
    private String number;
    private BigDecimal balance;
    private Account account;

    public CardImpl(int id, String number, BigDecimal balance, Account account) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Account getAccount() {
        return account;
    }

    public int addMoney() {
        return 0;
    }

    @Override
    public String toString() {
        return "CardImpl{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                '}';
    }
}
