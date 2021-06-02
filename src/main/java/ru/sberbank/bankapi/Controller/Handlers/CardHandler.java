package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.bankapi.Service.ServiceImpl;

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
        ServiceImpl service = new ServiceImpl();
        String response;

        if (exchange.getRequestMethod().equals("GET")) {
            response = service.getBalance(cardId);
        }
        else {
            String json= new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            response = service.addMoneyToCard(json, cardId);
            System.out.println(response);
        }
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
            System.out.println(exchange.getAttribute("login").toString());

    }

    private static long getIdFromUri(String uri) {
        String[] strings = uri.split("/");
        return Long.parseLong(strings[strings.length - 1]);
    }
}
