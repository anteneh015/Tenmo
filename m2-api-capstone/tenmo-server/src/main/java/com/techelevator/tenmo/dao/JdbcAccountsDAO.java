package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountsDAO implements AccountsDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double returnAccountBalance(String username) {
        Accounts accounts = new Accounts();
        String sql = "SELECT balance FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, username);
        while (rows.next()) {
            accounts.setBalance(rows.getDouble("balance"));
        }
        return accounts.getBalance();
    }
    @Override
    public Accounts getsAccountsByUsername(String username){
        String sql = "SELECT account_id, balance, username, users.user_id AS user_id " +
                "FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, username);
        if (rows.next()){
            Accounts accounts = mapRowToAccounts(rows);
            return accounts;
        }
        return null;
    }

    @Override
    public List<String> getAllUsernames() {
        List<String> usernameList = new ArrayList<String>();
        String sql = "SELECT username FROM users";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            usernameList.add(rows.getString("username"));
        }
        return usernameList;
    }

    @Override
    public void transferMoney(String usernameFrom, String usernameTo, double transferAmount) {
        Accounts accountsFrom = getsAccountsByUsername(usernameFrom);
        Accounts accountsTo = getsAccountsByUsername(usernameTo);
        if (accountsFrom.getBalance() >= transferAmount) {
            withdrawForTransfer(accountsFrom.getBalance(), transferAmount, accountsFrom.getUserId());
            depositForTransfer(accountsTo.getBalance(), transferAmount, accountsTo.getUserId());
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    @Override
    public void withdrawForTransfer (double balance, double transferAmount, Long userId){
        String sql = "UPDATE accounts SET balance = ?  WHERE user_id = ?";
        jdbcTemplate.update(sql, balance - transferAmount, userId);

    }

    @Override
    public void depositForTransfer ( double balance, double transferAmount, Long userId){
        String sql = "UPDATE accounts SET balance = ?  WHERE user_id = ?";
        jdbcTemplate.update(sql, balance + transferAmount, userId);
    }

    private Accounts mapRowToAccounts(SqlRowSet row){
        Accounts accounts = new Accounts();

        accounts.setBalance(row.getDouble("balance"));
        accounts.setAccountId(row.getLong("account_id"));
        accounts.setUserId(row.getLong("user_id"));
        accounts.setUsername(row.getString("username"));

        return accounts;
    }
}
