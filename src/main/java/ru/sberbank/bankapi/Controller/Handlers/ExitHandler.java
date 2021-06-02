package ru.sberbank.bankapi.Controller.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.eclipse.jetty.http.HttpParser;
import ru.sberbank.bankapi.Controller.Server;
import ru.sberbank.bankapi.DataAccessObject.DBConnector;

import java.io.IOException;
import java.io.OutputStream;

import static ru.sberbank.bankapi.Controller.Server.stopServer;

public class ExitHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Application closed";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
        DBConnector.closeConnection();
        System.exit(0);
    }
}
