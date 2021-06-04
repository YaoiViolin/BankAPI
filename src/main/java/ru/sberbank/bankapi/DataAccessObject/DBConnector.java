package ru.sberbank.bankapi.DataAccessObject;

import java.io.BufferedReader;
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
     * заполняет БД данными
     * @return 1 в случае успешного заполнения БД, 0 в случае ошибки
     */
    public static int dbInit() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO CLIENT (LOGIN) VALUES ( 'RITA'); " +
                    "INSERT INTO CLIENT (LOGIN) VALUES ( 'JOHN');" +
                    "INSERT INTO CLIENT (LOGIN) VALUES ( 'VASYA')");

            statement.executeUpdate("INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ( '8888', 120.5, 1 ); " +
                    "INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ('9999', 340.6, 2)");

            statement.executeUpdate("INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000000', 1 );" +
                    "INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000001', 1 ); " +
                    "INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000002', 2 ); " +
                    "INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000003', 2 )");

            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static int dbInit(int intValue) {
        BufferedReader reader;
        return 0;
    }


}
