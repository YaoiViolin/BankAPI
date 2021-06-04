package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.bankapi.Service.ServiceImpl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Обработчик, отвечающий за манипуляции со списком карт клиента
 * Сюда приходят GET запросы на вывод списка всех карт
 * и POST запросы на добавление новой карты
 */
public class CardsListHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserServiceImpl service = new UserServiceImpl();
        String response;
        int responseCode;
        if (exchange.getRequestMethod().equals("POST")) {
            String json= new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String data = service.createNewCard(json);
            response = data == null ? "Invalid account id" : data;
        }
        else {
            String login = getLoginFromUri(exchange.getRequestURI().toString());
            String data = service.getAllCards(login.toUpperCase(Locale.ROOT));
            response = data == null ? "No cards" : data;
        }
        switch (response){
            case "Invalid account id":
                responseCode = 404;
                break;
            case "No cards":
                responseCode = 204;
                break;
            default:
                responseCode = 200;
                break;
        }
        exchange.sendResponseHeaders(responseCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
    }

    private String getLoginFromUri(String uri) {
        String[] strings = uri.split("/");
        return strings[2];
    }
}
