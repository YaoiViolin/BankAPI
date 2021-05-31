package ru.sberbank.bankapi.Controller;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {
    HttpServer server;

    public int startServer() throws IOException {
    int serverPort = 8000;
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        CardHandler cardHandler = new CardHandler();
        server.createContext("/clients/", cardHandler::handle);


        server.setExecutor(null);
        // creates a default executor
        System.out.println("Server started");
        server.start();
        return 1;
    }
}
