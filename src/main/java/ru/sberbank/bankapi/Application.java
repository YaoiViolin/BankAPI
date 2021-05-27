package ru.sberbank.bankapi;

import ru.sberbank.bankapi.DataAccessObject.DBConnector;
import ru.sberbank.bankapi.DataAccessObject.domain.*;
import ru.sberbank.bankapi.DataAccessObject.repo.*;

import java.sql.*;
import java.util.List;

public class Application {
    public static final String DB_URL = "jdbc:h2:/Users/u19223229/IdeaProjects/BankAPI/src/main/java/ru/sberbank/bankapi/db/MainDataBase";
    public static final String USER = "user";
    public static final String PASSWORD = "password";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static final String DB_Driver = "org.h2.Driver";

    public static void main(String[] args) {
        DBConnector connector = new DBConnector();
        connector.createConnection();
        Client client = new ClientImpl(1, "RITA");
        List<Account> accounts = client.getAccounts();
        Account account = accounts.get(0);
        List<Card> cards = account.getCardsList();
        cards.forEach(System.out::println);
        Card card = cards.get(0);
        connector.closeConnection();

    }

}
