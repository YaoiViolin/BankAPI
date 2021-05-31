package ru.sberbank.bankapi.Service;

import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.Client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static ru.sberbank.bankapi.DataAccessObject.domain.Card.*;
import static ru.sberbank.bankapi.DataAccessObject.domain.Client.*;
import static ru.sberbank.bankapi.Service.ObjectToJsonConverter.*;

public class ServiceImpl implements Service{

    @Override
    public String addMoneyToCard(String jsonSumString, long cardId) {
        BigDecimal sum = new BigDecimal(covertJsonToMap(jsonSumString).get("sum"));
        Card card = Card.getCard(cardId);
        BigDecimal prev = card.getBalance();
        card.updateCardBalance(prev.add(sum));
        return card.getBalance().toString();
    }

    @Override
    public String getBalance(long id) {
        Card card = Card.getCard(id);
        BigDecimal balance = card.getBalance().setScale(2, RoundingMode.DOWN);
        return convertCardBalanceToJson(Collections.singletonMap("balance", balance));
    }

    @Override
    public String getAllCards(String clientLogin) {
        Client client = getClient(clientLogin);
        List<Card> cardsList = new ArrayList<>();

        client.getAccounts().forEach(account -> cardsList.addAll(account.getCards()));
        return convertListToJsonString(cardsList);

    }

    @Override
    public String createNewCard(String accountIdString) {
        String lastCardNumber = getLastCardNumber();
        Long number = Long.parseLong(lastCardNumber);
        number ++;
        String currentNum = String.format("%016d", number);
        Card card = new Card(0, currentNum);
        long accountId = Long.parseLong(covertJsonToMap(accountIdString).get("info"));
        Card.addCard(card, accountId);
        return convertSingleCardToJson(getCard(currentNum));
    }
}
