package ru.sberbank.bankapi.Service.ServiceImpl;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.CounterParty;
import ru.sberbank.bankapi.Service.InterFaces.CounterPartyService;
import ru.sberbank.bankapi.Service.InterFaces.UserService;
import ru.sberbank.bankapi.Service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.sberbank.bankapi.Service.ServiceImpl.ObjectToJsonConverter.convertJsonToTransAction;

public class CounterPartyServiceImpl implements CounterPartyService {

    @Override
    public String getTransactionsList() {
        List<Object> listOfTransactions = new ArrayList<>(CounterParty.getAll());
        if (listOfTransactions.size() == 0)
            return null;
        else
        return ObjectToJsonConverter.convertListToJsonString(listOfTransactions);
    }

    @Override
    public String makeTransaction(String jsonString) {
        CounterParty cp = convertJsonToTransAction(jsonString);
        UserService service = new UserServiceImpl();
        long cardFromId = 0;
        long cardToId = 0;
        try {
            cardFromId = Objects.requireNonNull(Card.getCard(cp.getCardNumberFrom())).getId();
            cardToId = Objects.requireNonNull(Card.getCard(cp.getCardNumberTo())).getId();
        } catch (NullPointerException e) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(service.withdrawMoneyFromCard(cp.getSum().toString(), cardFromId));
        stringBuilder.append(service.addMoneyToCard(cp.getSum().toString(), cardToId, false));
        CounterParty.createTransaction(cp);
        return stringBuilder.toString();
    }

}
