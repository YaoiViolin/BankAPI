package ru.sberbank.bankapi.Service.ServiceImpl;

import ru.sberbank.bankapi.DataAccessObject.domain.Account;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.Client;
import ru.sberbank.bankapi.Service.Interfaces.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.domain.Card.getCard;
import static ru.sberbank.bankapi.DataAccessObject.domain.Card.getLastCardNumber;
import static ru.sberbank.bankapi.DataAccessObject.domain.Client.getClient;
import static ru.sberbank.bankapi.Service.ObjectToJsonConverter.*;

/**
 * Класс сервиса клиента
 */
public class UserServiceImpl implements UserService {

    /**
     * Пополняет счёт карты
     * @param sumString строка в формате json вида {"sum":00.00} или строка с суммой со вторым знаком после запятой, например "12.89"
     * @param cardId карты, которую надо пополнить
     * @param fromJson true, если строка на входе передается в json, иначе false
     * @return получившийся баланс карты в формате json
     */
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

    /**
     * Снимает с баланса счёта карты sum
     * @param sumString строка с суммой со вторым знаком после запятой
     * @param cardId карты, с которой надо снять деньги
     * @return получившийся баланс карты
     * @throws ArithmeticException в случае, если на карте недостаточно денег
     */
    @Override
    public String withdrawMoneyFromCard(String sumString, long cardId) throws ArithmeticException {
        BigDecimal sum = new BigDecimal(sumString);
        Card card = Card.getCard(cardId);
        if (card == null)
            return null;
        else {
            BigDecimal prev = card.getBalance();
            if (checkIfWithdrawalPossible(prev, sum))
                card.setCardBalance(prev.subtract(sum));
            else throw new ArithmeticException();
            return convertCardBalanceToJson(Collections.singletonMap("balance", card.getBalance()));
        }
    }

    /**
     * Возвращает баланс счёта карты
     * @param cardId идентификатор карты
     * @return баланс карты в формате json
     */
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

    /**
     * @param clientLogin Логин клиента
     * @return список карт клиента в формате json
     */
    @Override
    public String getAllCards(String clientLogin) {
        Client client = getClient(clientLogin);
        List<Object> cardsList = new ArrayList<>();

        client.getAccounts().forEach(account -> cardsList.addAll(account.getCards()));
        return convertListToJsonString(cardsList);

    }

    /**
     * Созддает новую карту, привязанную к сч>ту
     * @param accountIdString идентификатор счёта в формате json вида {"id":0}
     * @return всю инф-ю по созданной карте в формате json
     */
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

    /**
     * Проверяет, достаточно ли денег на карте
     * @param cardBalance баланс карты
     * @param sum сумма, которую предполагается снять
     * @return true, в случае если снятие возможно, иначе false
     */
    boolean checkIfWithdrawalPossible (BigDecimal cardBalance, BigDecimal sum) {
        return cardBalance.compareTo(sum) >= 0;
    }
}
