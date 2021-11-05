package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Users;

import java.util.List;

public interface UsersDAO {
    public List<Users> getAllUsers();
    public List<String> getAllUsernames();
    public int getAccountIdFromUsername(String username);
}
