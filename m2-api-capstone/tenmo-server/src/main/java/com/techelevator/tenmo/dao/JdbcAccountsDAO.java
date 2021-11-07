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
    public List<Accounts> getsAccountsByUsername(String username){
        List<Accounts> accountsList = new ArrayList<Accounts>();
        String sql = "SELECT account_id, balance, username, users.user_id AS user_id " +
                "FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, username);
        if (rows.next()){
            accountsList.add(mapRowToAccounts(rows));
        }
        return accountsList;
    }

    @Override
    public void updateAccountBalance(Accounts accounts) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, accounts.getBalance(), accounts.getAccountId());
    }

    @Override
    public List<Accounts> getAccountsList() {
        List<Accounts> accountsList = new ArrayList<Accounts>();
        String sql = "SELECT account_id, balance, username, users.user_id AS user_id " +
                "FROM accounts JOIN users ON accounts.user_id = users.user_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            accountsList.add(mapRowToAccounts(rows));
        }
        return accountsList;
    }

    @Override
    public List<Accounts> getAccountsById(int accountId) {
        List<Accounts> accountsList = new ArrayList<Accounts>();
        String sql = "SELECT account_id, balance, username, users.user_id AS user_id FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE account_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId);
        while (rows.next()) {
            accountsList.add(mapRowToAccounts(rows));
        }
        return accountsList;
    }

    @Override
    public String getUserFromAccountId(int accountId) {
        String username = null;
        String sql = "SELECT username FROM users JOIN accounts ON users.user_id = accounts.user_id WHERE account_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId);
        while (rows.next()) {
            username = rows.getString("username");
        }
        return username;
    }

    private Accounts mapRowToAccounts(SqlRowSet row){
        Accounts accounts = new Accounts();

        accounts.setBalance(row.getDouble("balance"));
        accounts.setAccountId(row.getInt("account_id"));
        accounts.setUserId(row.getInt("user_id"));
        accounts.setUsername(row.getString("username"));

        return accounts;
    }
}
