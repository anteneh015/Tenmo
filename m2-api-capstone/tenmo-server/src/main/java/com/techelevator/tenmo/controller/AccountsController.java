package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.dao.UsersDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountsController {

    private AccountsDAO accountsDAO;
    private UsersDAO usersDAO;
    private TransfersDAO transfersDAO;

    public AccountsController(AccountsDAO accountsDAO, UsersDAO usersDAO, TransfersDAO transfersDAO) {
        this.accountsDAO = accountsDAO;
        this.usersDAO = usersDAO;
        this.transfersDAO = transfersDAO;
    }

//    @RequestMapping(path = "accounts", method = RequestMethod.GET)
//    public Accounts displayAccount(Principal principal) {
//        return accountsDAO.getsAccountsByUsername(principal.getName());
//    }

    @RequestMapping(path = "accounts", method = RequestMethod.GET)
    public List<Accounts> displayAccounts() {
        List<Accounts> accountsList = new ArrayList<Accounts>();
        accountsList = accountsDAO.getAccountsList();
        return accountsList;
    }

    @RequestMapping(path = "accounts/balance", method = RequestMethod.GET)
    public double getAccountBalance(Principal principal) {
        return accountsDAO.returnAccountBalance(principal.getName());
    }

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<Users>();
        usersList = usersDAO.getAllUsers();
        return usersList;
    }

    @RequestMapping(path = "users/usernames", method = RequestMethod.GET)
    public List<String> getAllUsername() {
        List<String> usernameList = new ArrayList<String>();
        usernameList = usersDAO.getAllUsernames();
        return usernameList;
    }

    @RequestMapping(path = "users/{username}/accountId", method = RequestMethod.GET)
    public int getAccountId(@PathVariable String username) {
        int accountId = usersDAO.getAccountIdFromUsername(username);
        return accountId;
    }

    @RequestMapping(path = "accounts", method = RequestMethod.PUT)
    public void update(Accounts accountTo, Accounts accountFrom, double amount) {
        transfersDAO.transferMoney(accountFrom.getUsername(),accountTo.getUsername(),amount);
    }

    @RequestMapping(path = "accounts/transfers", method = RequestMethod.GET)
    public List<Transfers> getTransfersFromAccountId(@RequestBody Accounts accounts) {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        transfersList = transfersDAO.getTransfersFromUserId(accounts.getAccountId());
        return transfersList;
    }

    @RequestMapping(path = "accounts/transfers/{transfer_status_id}", method = RequestMethod.GET)
    public List<Transfers> getTransferListFromTransferStatusId(@PathVariable int transfer_status_id,@RequestBody Accounts accounts) {
        if(transfer_status_id == 2 || transfer_status_id == 3) {
            return transfersDAO.getPastTransfersFromUserId(accounts.getAccountId());
        } else if(transfer_status_id == 1) {
            return transfersDAO.getCurrentTransfersFromUserId(accounts.getAccountId());
        }
        return null;
    }

    @RequestMapping(path = "accounts/transfers", method = RequestMethod.POST)
    public Transfers addTransfer(int accountTo, int accountFrom, double transferAmount){
        return transfersDAO.sendMoneyTransferCreation(accountTo, accountFrom, transferAmount);
    }

}
