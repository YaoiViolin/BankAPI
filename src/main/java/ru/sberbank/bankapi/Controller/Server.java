package ru.sberbank.bankapi.Controller;

import com.sun.net.httpserver.HttpServer;
import ru.sberbank.bankapi.Controller.Handlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {
    public static HttpServer server;

    public int startServer() throws IOException {
    int serverPort = 8000;
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        RootHandler rootHandler = new RootHandler();

        server.createContext("/clients/", rootHandler);
        //server.createContext("/clients/cards/", cardHandler::handle);
        //server.createContext("/clients/balance/", cardHandler::handle);
        //server.createContext("/clients/balance", cardHandler::handle);


        server.setExecutor(null);
        // creates a default executor
        System.out.println("Server started");
        server.start();
        return 1;
    }
}
