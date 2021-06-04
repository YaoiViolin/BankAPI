package ru.sberbank.bankapi.Service.Interfaces;

public interface CounterPartyService {
    String getTransactionsList();
    String makeTransaction(String jsonString);
}
