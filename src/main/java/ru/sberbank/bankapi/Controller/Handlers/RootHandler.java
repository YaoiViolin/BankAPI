package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.regex.Pattern;

public class RootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String URI = exchange.getRequestURI().toString();
        System.out.println(URI);


        if (Pattern.matches("/clients/\\D+/cards/$", URI)) {
            CardsListHandler cardsListHandler = new CardsListHandler();
            cardsListHandler.handle(exchange);
        }
        else {
            CardHandler handler = new CardHandler();
            handler.handle(exchange);
        }
    }
}
