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
        String sql = "SELECT user_id, username FROM users";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            Users users = new Users();
            users.setUserId(rows.getInt("user_id"));
            users.setUsername(rows.getString("username"));
            usersList.add(users);
        }
        return usersList;
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
