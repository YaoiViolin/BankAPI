package ru.sberbank.bankapi.DataAccessObject.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ru.sberbank.bankapi.DataAccessObject.repo.AccountRepo;
import ru.sberbank.bankapi.DataAccessObject.repo.ClientRepo;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.con;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.rs;

@JsonAutoDetect
public class Client implements ClientRepo {
    private int id;
    private String login;

    public Client(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public static Client getClient(String login) {
        try {
            Client client = null;
            PreparedStatement statement = con.prepareStatement("SELECT * FROM CLIENT WHERE LOGIN = ?");
            statement.setString(1, login);
            rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                client = new Client(id, login);
            }
            return client;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ACCOUNT WHERE CLIENT_ID = ?");
            statement.setInt(1, this.id);
            rs = statement.executeQuery();
            while (rs.next()) {
                int accountId = rs.getInt("ID");
                String accountNumber = rs.getString("NUMBER");
                BigDecimal balance = rs.getBigDecimal("BALANCE");
                accounts.add(new Account(accountId, accountNumber, balance));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return accounts;
    }
}
