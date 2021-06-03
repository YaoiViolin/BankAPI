package ru.sberbank.bankapi.Service.InterFaces;

public interface CounterPartyService {
    String getTransactionsList();
    String makeTransaction(String jsonString);
}
