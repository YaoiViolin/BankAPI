package ru.sberbank.bankapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sberbank.bankapi.Controller.Server;
import ru.sberbank.bankapi.DataAccessObject.domain.Card;
import ru.sberbank.bankapi.DataAccessObject.domain.CounterParty;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

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




