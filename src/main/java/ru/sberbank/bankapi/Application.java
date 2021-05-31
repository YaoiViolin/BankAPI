package ru.sberbank.bankapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sberbank.bankapi.Controller.ObjectToJsonConverter;
import ru.sberbank.bankapi.Controller.Server;
import ru.sberbank.bankapi.DataAccessObject.DBConnector;
import ru.sberbank.bankapi.DataAccessObject.domain.*;
import ru.sberbank.bankapi.DataAccessObject.repo.*;
import ru.sberbank.bankapi.Service.Service;
import ru.sberbank.bankapi.Service.ServiceImpl;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;


public class Application {


    public static void main(String[] args) throws IOException {
//        Server server = new Server();
//        server.startServer();


        DBConnector connector = new DBConnector();
        connector.createConnection();


        Service service = new ServiceImpl();
        //System.out.println(service.getBalance("1234123412341234"));
        //service.addMoneyToCard(BigDecimal.TEN, "1234123412341234");
        //System.out.println(service.getBalance("1234123412341234"));
//        Client client = Client.getClient("RITA");
//        List<Account> accounts = client.getAccounts();
//        Account account = accounts.get(0);
//        List<Card> card;

        String str = service.getAllCards("RITA");

        System.out.println(str);

        connector.closeConnection();


    }
}




