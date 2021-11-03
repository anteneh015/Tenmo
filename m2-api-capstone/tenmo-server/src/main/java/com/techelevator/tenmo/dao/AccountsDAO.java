package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;

public interface AccountsDAO {

    public double returnAccountBalance(String username);
    public Accounts getsAccountsByUsername(String username);
    public void transferMoney(String usernameFrom, String usernameTo, double transferAmount);
    public void withdrawForTransfer (double balance, double transferAmount, Long userId);
    public void depositForTransfer ( double balance, double transferAmount, Long userId);


}
