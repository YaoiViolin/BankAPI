package ru.sberbank.bankapi;

import ru.sberbank.bankapi.DataAccessObject.DBConnector;
import ru.sberbank.bankapi.DataAccessObject.domain.AccountImpl;
import ru.sberbank.bankapi.DataAccessObject.domain.ClientImpl;
import ru.sberbank.bankapi.DataAccessObject.repo.Account;
import ru.sberbank.bankapi.DataAccessObject.repo.Card;
import ru.sberbank.bankapi.DataAccessObject.repo.Client;

import java.math.BigDecimal;
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
        Client client = new ClientImpl();

        connector.closeConnection();

    }

}
