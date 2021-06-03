package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.bankapi.Service.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CardHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String URI = exchange.getRequestURI().toString();
        long cardId = getIdFromUri(URI);
        int responseCode;
        UserServiceImpl service = new UserServiceImpl();
        String response;

        if (exchange.getRequestMethod().equals("GET")) {
            String data = service.getBalance(cardId);
            response = data == null ? "No such card found" : data;
        }
        else {
            String json= new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String data = service.addMoneyToCard(json, cardId, true);
            response = data == null ? "No such card found" : data;
        }
        responseCode = response.equals("No such card found") ? 404 : 200;

            exchange.sendResponseHeaders(responseCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
    }

    private static long getIdFromUri(String uri) {
        String[] strings = uri.split("/");
        return Long.parseLong(strings[strings.length - 1]);
    }
}
