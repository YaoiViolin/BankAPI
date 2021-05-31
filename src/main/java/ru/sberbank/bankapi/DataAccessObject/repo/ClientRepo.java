package ru.sberbank.bankapi.DataAccessObject.repo;

import ru.sberbank.bankapi.DataAccessObject.domain.Account;

import java.util.List;

public interface ClientRepo {

    List<Account> getAccounts();
}
