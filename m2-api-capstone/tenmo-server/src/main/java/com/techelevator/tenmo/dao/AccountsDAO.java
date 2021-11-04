package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

import java.util.List;

public interface AccountsDAO {

    public double returnAccountBalance(String username);
    public Accounts getsAccountsByUsername(String username);
    public void transferMoney(String usernameFrom, String usernameTo, double transferAmount);
    public void withdrawForTransfer (double balance, double transferAmount, int userId);
    public void depositForTransfer (double balance, double transferAmount, int userId);
    public List<Accounts> getAccountsList();

}
