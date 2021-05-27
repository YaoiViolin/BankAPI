package ru.sberbank.bankapi.DataAccessObject.domain;

import ru.sberbank.bankapi.DataAccessObject.repo.Account;
import ru.sberbank.bankapi.DataAccessObject.repo.Card;
import ru.sberbank.bankapi.DataAccessObject.repo.Client;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.bankapi.DataAccessObject.DBConnector.con;
import static ru.sberbank.bankapi.DataAccessObject.DBConnector.rs;

public class ClientImpl implements Client {
    private int id;
    private String login;

    public ClientImpl(int id, String login) {
        this.id = id;
        this.login = login;
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
                accounts.add(new AccountImpl(accountId, accountNumber, balance, this));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return accounts;
    }
}
