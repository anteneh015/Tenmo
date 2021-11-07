package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.util.List;

public interface AccountsDAO {

    public double returnAccountBalance(String username);
    public List<Accounts> getsAccountsByUsername(String username);
    public void updateAccountBalance(Accounts accounts);
    public List<Accounts> getAccountsList();
    public String getUserFromAccountId(int accountId);
    public List<Accounts> getAccountsById(int accountId);

}
