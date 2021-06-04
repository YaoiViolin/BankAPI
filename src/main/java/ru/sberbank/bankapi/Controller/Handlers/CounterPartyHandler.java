package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.bankapi.Service.Interfaces.CounterPartyService;
import ru.sberbank.bankapi.Service.ServiceImpl.CounterPartyServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Обработчик взаимодействия контрагентов
 * Сюда приходят GET запросы на получение информации о взаимодействии контрагентов
 * и POST запромсы на добавление инф-и о контрагентах
 */
public class CounterPartyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String URI = exchange.getRequestURI().toString();
        CounterPartyService service = new CounterPartyServiceImpl();
        String response = null;
        int responseCode = 200;

        if (exchange.getRequestMethod().equals("GET")) {
            String data = service.getTransactionsList();
            if (data == null) {
                response = "No transactions were made";
                responseCode = 404;
            } else response = data;
        }
        else {
            String json= new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String data = service.makeTransaction(json);
            if (data == null) {
                response = "No such cards";
                responseCode = 404;
            } else response = data;
        }
        exchange.sendResponseHeaders(responseCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}
