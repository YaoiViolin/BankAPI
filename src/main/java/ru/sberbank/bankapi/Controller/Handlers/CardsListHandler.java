package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.bankapi.Service.ServiceImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Collectors;

public class CardsListHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ServiceImpl service = new ServiceImpl();
        String response = null;
        if (exchange.getRequestMethod().equals("POST")) {
            System.out.println("This is POST");
            String json= new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            response = service.createNewCard(json);

        }
        else {
            System.out.println("This is get");
            String login = getLoginFromUri(exchange.getRequestURI().toString());
            response = service.getAllCards(login.toUpperCase(Locale.ROOT));
        }
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
            System.out.println(exchange.getAttribute("login").toString());

    }

    private String getLoginFromUri(String uri) {
        String[] strings = uri.split("/");
        return strings[2];
    }
}
