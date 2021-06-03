package ru.sberbank.bankapi.Service;

public interface CounterPartyService {
    String getTransactionsList();
    String makeTransaction(String jsonString);
}
