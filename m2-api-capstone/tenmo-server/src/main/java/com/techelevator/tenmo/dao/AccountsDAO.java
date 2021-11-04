package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.util.List;

public interface AccountsDAO {

    public double returnAccountBalance(String username);
    public Accounts getsAccountsByUsername(String username);

    public List<Accounts> getAccountsList();

}
