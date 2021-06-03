package ru.sberbank.bankapi;

import java.io.IOException;

import static ru.sberbank.bankapi.Controller.Server.startServer;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.createConnection;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.dbInit;


public class Application {

    public static void main(String[] args) throws IOException {
        createConnection();
        dbInit();
        startServer();

    }
}




