package ru.sberbank.bankapi.Controller;

import com.sun.net.httpserver.HttpServer;
import ru.sberbank.bankapi.Controller.Handlers.CounterPartyHandler;
import ru.sberbank.bankapi.Controller.Handlers.ExitHandler;
import ru.sberbank.bankapi.Controller.Handlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Класс отвечающий за создание сервера
 */
public class Server {
    public static HttpServer server;

    /**
     * Запускает сервер
     * @return 1 в случае успеха
     */
    public static int startServer() throws IOException {
    int serverPort = 8080;
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        RootHandler rootHandler = new RootHandler();
        ExitHandler exitHandler = new ExitHandler();
        CounterPartyHandler counterPartyHandler = new CounterPartyHandler();

        server.createContext("/exit", exitHandler);
        server.createContext("/clients/", rootHandler);
        server.createContext("/counterparty/", counterPartyHandler);
        server.setExecutor(null);
        server.start();
        return 1;
    }

    /**
     * Завершает работу сервера
     */
    public static void stopServer() {
        try {
            server.stop(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
