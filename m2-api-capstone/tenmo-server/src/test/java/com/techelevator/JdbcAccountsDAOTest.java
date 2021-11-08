package com.techelevator;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.JdbcAccountsDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Users;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;

public class JdbcAccountsDAOTest extends DAOIntegrationTest{

    private AccountsDAO accounts;
    private JdbcTemplate jdbcTemplate;
    private static final String TEST_USER = "test";

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        accounts = new JdbcAccountsDAO(jdbcTemplate);
        String sql = "INSERT INTO users (user_id, username, password_hash) VALUES (DEFAULT, ?, 'test')";
        jdbcTemplate.update(sql, TEST_USER);
        String sqlTwo = "SELECT user_id, username, password_hash FROM users WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlTwo,TEST_USER);
        Users testUser = new Users();
        while (rows.next()) {
            testUser.setPassHash(rows.getString("password_hash"));
            testUser.setUserId(rows.getInt("user_id"));
            testUser.setUsername(TEST_USER);
        }
    }

    @After
    public void rollback() throws SQLException {
        getDataSource().getConnection().rollback();
    }

    @Test
    public void update_balance_test() {
        Accounts originalAccount = createAccount(20);
        Accounts newAccount = originalAccount;
        newAccount.setBalance(30);
        insertAccount(originalAccount);
        accounts.updateAccountBalance(newAccount);
        Assert.assertTrue(30 == originalAccount.getBalance());
    }

    private void insertAccount(Accounts accounts) {
        String sql = "INSERT INTO accounts (account_id, user_id, balance) VALUES (DEFAULT, ?, ?) RETURNING account_id";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, accounts.getUserId(), accounts.getBalance());
        accounts.setAccountId(accountId);
    }

    private Accounts createAccount(double balance) {
        String sqlTwo = "SELECT user_id, username, password_hash FROM users WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlTwo,TEST_USER);
        Users testUser = new Users();
        while (rows.next()) {
            testUser.setPassHash(rows.getString("password_hash"));
            testUser.setUserId(rows.getInt("user_id"));
            testUser.setUsername(TEST_USER);
        }
        Accounts accounts = new Accounts();
        accounts.setBalance(balance);
        accounts.setUserId(testUser.getUserId());
        return accounts;
    }

}
