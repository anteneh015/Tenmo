package com.techelevator.tenmo.models;

public class Accounts {
    private double balance;
    private int accountId;
    private int userId;
    private String username;

    public String getOwnerUsername() {
        return username;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.username = ownerUsername;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}