package com.techelevator;

import com.techelevator.tenmo.auth.model.User;
import com.techelevator.tenmo.dao.JdbcUsersDAO;
import com.techelevator.tenmo.dao.UsersDAO;
import com.techelevator.tenmo.model.Users;
import jdk.jfr.BooleanFlag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class JdbcUsersDAOTest extends DAOIntegrationTest{

    private UsersDAO users;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        users = new JdbcUsersDAO(jdbcTemplate);
    }

    @After
    public void rollback() throws SQLException {
        getDataSource().getConnection().rollback();
    }

//    @Test
//    public void test_get_all_users() {
//        truncateUsers();
//        Users one = createUser("test", "testing");
//        insertUser(one);
//        Users two = createUser("test2","testing");
//        insertUser(two);
//        List<Users> userList = users.getAllUsers();
//        Assert.assertEquals(2, userList.size());
//    }

    @Test
    public void get_all_usernames_test() {
        truncateUsers();
        Users one = createUser("test", "testing");
        insertUser(one);
        Users two = createUser("test2","testing");
        insertUser(two);
        List<String> usernameList = users.getAllUsernames();
        Assert.assertEquals(2,usernameList.size());
    }

    //int userId,  String passHash, int accountId
    private Users createUser(String username, String passHash) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassHash(passHash);
        return user;
    }

    private void insertUser(Users user) {
        String sql = "INSERT INTO users (user_id, username, password_hash) VALUES (DEFAULT, ?, ?) RETURNING user_id";
        Integer userId = jdbcTemplate.queryForObject(sql, Integer.class, user.getUsername(), user.getPassHash());
        user.setUserId(userId);
    }

    private void truncateUsers() {
        jdbcTemplate.update("TRUNCATE users CASCADE");
    }

}
