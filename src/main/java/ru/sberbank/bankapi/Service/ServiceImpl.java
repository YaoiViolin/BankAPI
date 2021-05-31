package ru.sberbank.bankapi.Service;

import ru.sberbank.bankapi.Controller.ObjectToJsonConverter;
import ru.sberbank.bankapi.DataAccessObject.domain.Account;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.Client;
import ru.sberbank.bankapi.DataAccessObject.repo.AccountRepo;
import ru.sberbank.bankapi.DataAccessObject.repo.CardRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.domain.Card.*;
import static ru.sberbank.bankapi.DataAccessObject.domain.Client.*;

public class ServiceImpl implements Service{

    @Override
    public int addMoneyToCard(BigDecimal sum, String number) {
        Card card = CardRepo.getCard(number);
        BigDecimal prev = card.getBalance();
        card.updateCardBalance(prev.add(sum));
        return 1;
    }

    @Override
    public BigDecimal getBalance(String cardNumber) {
        Card card = CardRepo.getCard(cardNumber);
        BigDecimal bigDecimal = card.getBalance().setScale(2, RoundingMode.DOWN);
        System.out.println(bigDecimal);
        return bigDecimal;
    }

    @Override
    public String getAllCards(String clientLogin) {
        Client client = getClient(clientLogin);
        List<Card> cardsList = new ArrayList<>();

        client.getAccounts().forEach(account -> cardsList.addAll(account.getCards()));
        return ObjectToJsonConverter.convertListToJsonString(cardsList);

    }

    @Override
    public int createNewCard(Account account) {
        String lastCardNumber = getLastCardNumber();
        Long number = Long.parseLong(lastCardNumber);
        Card card = new Card(0, (number++).toString());
        CardRepo.addCard(card, account.getId());
        return 1;
    }
}
