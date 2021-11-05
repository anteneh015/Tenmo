package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Users;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUsersDAO implements UsersDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcUsersDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<Users>();
        String sql = "SELECT users.user_id AS userid, username, account_id FROM users JOIN accounts ON users.user_id = accounts.user_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            Users users = new Users();
            users.setUserId(rows.getInt("userid"));
            users.setUsername(rows.getString("username"));
            users.setAccountId(rows.getInt("account_id"));
            usersList.add(users);
        }
        return usersList;
    }

    @Override
    public int getAccountIdFromUsername(String username) {
        int accountId = 0;
        String sql = "SELECT account_id FROM users JOIN accounts ON users.user_id = accounts.user_id WHERE username = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, username);
        if (rows.next()) {
            accountId = rows.getInt("account_id");
        }
        return accountId;
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

}
