package ru.sberbank.bankapi.Controller;

import com.sun.net.httpserver.HttpServer;
import ru.sberbank.bankapi.Controller.Handlers.ExitHandler;
import ru.sberbank.bankapi.Controller.Handlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {
    public static HttpServer server;

    public static int startServer() throws IOException {
    int serverPort = 8080;
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        RootHandler rootHandler = new RootHandler();
        ExitHandler exitHandler = new ExitHandler();

        server.createContext("/exit", exitHandler);
        server.createContext("/clients/", rootHandler);
        server.setExecutor(null);
        server.start();
        System.out.println("Server started");
        return 1;
    }

    public static void stopServer() {
        try {
            server.stop(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
