package ru.sberbank.bankapi.DataAccessObject;

import java.sql.*;

public class DBConnector {
    public static final String DB_URL = "jdbc:h2:/Users/u19223229/IdeaProjects/BankAPI/src/main/java/ru/sberbank/bankapi/db/MainDataBase";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DB_Driver = "org.h2.Driver";

    public static Connection con;
    public static ResultSet rs;


    public int createConnection() {
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

    public int closeConnection() {
        try {
            con.close();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }
}
