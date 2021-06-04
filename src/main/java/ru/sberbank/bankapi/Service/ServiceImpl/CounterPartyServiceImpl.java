package ru.sberbank.bankapi.Service.ServiceImpl;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.CounterParty;
import ru.sberbank.bankapi.Service.Interfaces.CounterPartyService;
import ru.sberbank.bankapi.Service.Interfaces.UserService;
import ru.sberbank.bankapi.Service.ObjectToJsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.sberbank.bankapi.Service.ObjectToJsonConverter.convertJsonToTransAction;

/**
 * Класс сервисных функций взаимодействий контрагентов
 */
public class CounterPartyServiceImpl implements CounterPartyService {
    /**
     * @return список всех переводов между контрагентами
     */
    @Override
    public String getTransactionsList() {
        List<Object> listOfTransactions = new ArrayList<>(CounterParty.getAll());
        if (listOfTransactions.size() == 0)
            return null;
        else
        return ObjectToJsonConverter.convertListToJsonString(listOfTransactions);
    }

    /**
     * Осуществляет перевод между картами
     * @param jsonString с номерами карт и суммой перевода
     * @return получившийся баланс обеих карт в формате json, или null, в случае если на карте недостаточно денег
     */

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
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(service.withdrawMoneyFromCard(cp.getSum().toString(), cardFromId));
            stringBuilder.append(",");
            stringBuilder.append(service.addMoneyToCard(cp.getSum().toString(), cardToId, false));
            stringBuilder.append("]");
            CounterParty.createTransaction(cp);
            return stringBuilder.toString();
        } catch (ArithmeticException e) {
            return null;
        }
    }
}
