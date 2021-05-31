package ru.sberbank.bankapi.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CardHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            System.out.println("This is POST");

        }
        else
            System.out.println("This is get");

        String response = "This is the response";
        exchange.sendResponseHeaders(200, response.length());

        // need GET params here

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}
