package ru.sberbank.bankapi.DataAccessObject;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

/**
 * Класс обеспечиваюзий взаимодействие с БД
 */
public class DBConnector {

    public static final String DB_URL = "jdbc:h2:mem:Bank";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DB_Driver = "org.h2.Driver";

    public static Connection con;
    public static ResultSet rs;
    /**
     * Пути к файлам с ресурсами для заполнения БД
     */
    private static final String CLIENTS_RESOURCE_FILE = "src/main/resources/DBContents/clients.txt";
    private static final String ACCOUNTS_RESOURCE_FILE = "src/main/resources/DBContents/accounts.txt";
    private static final String CARDS_RESOURCE_FILE = "src/main/resources/DBContents/cards.txt";

    /**
     * выполняется подключение к БД, хранящейся в памяти
     * @return 1 в случае успешного подключения к БД, 0 в случае ошибки
     */
    public static int createConnection() {
        try {
            Class.forName(DB_Driver);
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            return 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL !");
            return 0;
        }
    }

    /**
     * закрывает подключение к БД
     * @return 1 в случае успешного завершения работы с БД, 0 в случае ошибки
     */
    public static int closeConnection() {
        try {
            con.close();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    /**
     * создает таблицы (схему) БД
     * @return 1 в случае успешного создания таблиц БД, 0 в случае ошибки
     */
    public static int dbCreate() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS CARD, ACCOUNT, CLIENT, COUNTERPARTY");

            statement.executeUpdate("create table if not exists CLIENT" +
                    "(ID BIGINT auto_increment " +
                    "primary key," +
                    "LOGIN VARCHAR(15) not null);" +
                    "create unique index if not exists CLIENT_LOGIN_UINDEX " +
                    "on CLIENT (LOGIN);");

            statement.executeUpdate("create table if not exists ACCOUNT" +
                    "(ID BIGINT auto_increment," +
                    "NUMBER VARCHAR(25)," +
                    "BALANCE DECIMAL(15,2) not null," +
                    "CLIENT_ID BIGINT not null " +
                    "references CLIENT (ID)," +
                    "constraint ACCOUNT_PK " +
                    "primary key (ID)); " +
                    "create unique index if not exists ACCOUNT_CLIENT_ID_UINDEX " +
                    "on ACCOUNT (CLIENT_ID); " +
                    "create unique index if not exists ACCOUNT_NUMBER_UINDEX " +
                    "on ACCOUNT (NUMBER);");

            statement.executeUpdate("create table if not exists CARD " +
                    "(ID BIGINT auto_increment," +
                    "NUMBER VARCHAR(16) not null," +
                    "ACCOUNT_ID BIGINT not null," +
                    "constraint CARD_PK " +
                    "primary key (ID)," +
                    "constraint FK_ACCOUNT_ID " +
                    "foreign key (ACCOUNT_ID) references ACCOUNT (ID));" +
                    "create unique index if not exists CARD_NUMBER_UINDEX " +
                    "on CARD (NUMBER);");

            statement.executeUpdate("create table if not exists COUNTERPARTY " +
                    "(TRNS_ID IDENTITY, " +
                    "CARD_FROM VARCHAR(16) not null, " +
                    "CARD_TO VARCHAR(16) not null, " +
                    "SUM DECIMAL, " +
                    "constraint COUNTERPARTY_PK " +
                    "primary key (TRNS_ID));");

            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    /**
     * заполняет БД данными из файлов
     * @see DBConnector приватные переменнные
     * @return 1 в случае успешного заполнения БД, 0 в случае ошибки
     */
    public static int dbInit() {
        try {
            clientsInit();
            accountsInit();
            cardsInit();
            return 1;
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    private static void clientsInit() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(DBConnector.CLIENTS_RESOURCE_FILE));
        PreparedStatement statement = con.prepareStatement("INSERT INTO CLIENT (LOGIN) VALUES ( ? ); ");
        while (reader.ready()) {
            String login = reader.readLine();
            statement.setString(1, login);
            statement.executeUpdate();
        }
    }

    private static void accountsInit() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(DBConnector.ACCOUNTS_RESOURCE_FILE));
        PreparedStatement statement = con.prepareStatement("INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES (?, ?, ?);");
        while (reader.ready()) {
            String[] accountParams = reader.readLine().split(";");
            statement.setString(1, accountParams[0]);
            statement.setBigDecimal(2, new BigDecimal(accountParams[1]));
            statement.setLong(3, Long.parseLong(accountParams[2]));
            statement.executeUpdate();
        }
    }

    private static void cardsInit() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(DBConnector.CARDS_RESOURCE_FILE));
        PreparedStatement statement = con.prepareStatement("INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( ?, ? );");
        while (reader.ready()) {
            String[] accountParams = reader.readLine().split(";");
            statement.setString(1, accountParams[0]);
            statement.setLong(2, Long.parseLong(accountParams[1]));
            statement.executeUpdate();
        }
    }


}
