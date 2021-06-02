package ru.sberbank.bankapi.DataAccessObject;

import java.sql.*;

public class DBConnector {
    //public static final String DB_URL = "jdbc:h2:/Users/u19223229/IdeaProjects/BankAPI/src/main/java/ru/sberbank/bankapi/db/Bank";
    public static final String DB_URL = "jdbc:h2:mem:Bank";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DB_Driver = "org.h2.Driver";

    public static Connection con;
    public static ResultSet rs;

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

    public static int closeConnection() {
        try {
            con.close();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public static int dbInit() {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS CARD, ACCOUNT, CLIENT");

            statement.executeUpdate("create table if not exists CLIENT" +
                    "(ID INT auto_increment " +
                    "primary key," +
                    "LOGIN VARCHAR(15) not null);" +
                    "create unique index if not exists CLIENT_LOGIN_UINDEX " +
                    "on CLIENT (LOGIN);");

            statement.executeUpdate("create table if not exists ACCOUNT" +
                    "(ID BIGINT auto_increment," +
                    "NUMBER VARCHAR(25)," +
                    "BALANCE DECIMAL not null," +
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


            statement.executeUpdate("INSERT INTO CLIENT (LOGIN) VALUES ( 'RITA'); " +
                    "INSERT INTO CLIENT (LOGIN) VALUES ( 'JOHN');" +
                    "INSERT INTO CLIENT (LOGIN) VALUES ( 'VASYA')");

            statement.executeUpdate("INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ( '8888', 12.5, 1 ); " +
                    "INSERT INTO ACCOUNT (NUMBER, BALANCE, CLIENT_ID) VALUES ('9999', 340.6, 2)");

            statement.executeUpdate("INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000000', 1 );" +
                    "INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000001', 1 ); " +
                    "INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000002', 2 ); " +
                    "INSERT INTO CARD(NUMBER, ACCOUNT_ID) VALUES ( '0000000000000003', 2 )");

            statement.executeQuery("SELECT * FROM CARD");


            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
}
