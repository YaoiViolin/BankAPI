package ru.sberbank.bankapi.Service;

import ru.sberbank.bankapi.DataAccessObject.domain.Account;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.Client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;

import static ru.sberbank.bankapi.DataAccessObject.domain.Card.*;
import static ru.sberbank.bankapi.DataAccessObject.domain.Client.*;
import static ru.sberbank.bankapi.Service.ObjectToJsonConverter.*;

public class UserServiceImpl implements UserService {

    @Override
    public String addMoneyToCard(String sumString, long cardId, boolean fromJson) {
        BigDecimal sum;
        if (fromJson)
            sum = new BigDecimal(covertJsonToMap(sumString).get("sum"));
        else
            sum = new BigDecimal(sumString);
        Card card = Card.getCard(cardId);
        if (card == null)
            return null;
        else {
            BigDecimal prev = card.getBalance();
            card.setCardBalance(prev.add(sum));
            return convertCardBalanceToJson(Collections.singletonMap("balance", card.getBalance()));
        }
    }

    @Override
    public String withdrawMoneyFromCard(String sumString, long cardId) {
        BigDecimal sum = new BigDecimal(sumString);
        Card card = Card.getCard(cardId);
        if (card == null)
            return null;
        else {
            BigDecimal prev = card.getBalance();
            if (prev.compareTo(sum) >= 0)
                card.setCardBalance(prev.subtract(sum));
            else throw new ArithmeticException();
            return convertCardBalanceToJson(Collections.singletonMap("balance", card.getBalance()));
        }
    }

    @Override
    public String getBalance(long cardId) {
        Card card = Card.getCard(cardId);
        if (card == null)
            return null;
        else {
            BigDecimal balance = card.getBalance().setScale(2, RoundingMode.DOWN);
            return convertCardBalanceToJson(Collections.singletonMap("balance", balance));
        }
    }

    @Override
    public String getAllCards(String clientLogin) {
        Client client = getClient(clientLogin);
        List<Object> cardsList = new ArrayList<>();

        client.getAccounts().forEach(account -> cardsList.addAll(account.getCards()));
        return convertListToJsonString(cardsList);

    }

    @Override
    public String createNewCard(String accountIdString){
        long accountId = Long.parseLong(covertJsonToMap(accountIdString).get("id"));
        if (Account.ifExists(accountId)) {
            String lastCardNumber = getLastCardNumber();
            Long number = Long.parseLong(lastCardNumber);
            number++;
            String currentNum = String.format("%016d", number);
            Card card = new Card(0, currentNum);
            Card.addCard(card, accountId);
            return convertSingleCardToJson(getCard(currentNum));
        }
        else return null;
    }
}
