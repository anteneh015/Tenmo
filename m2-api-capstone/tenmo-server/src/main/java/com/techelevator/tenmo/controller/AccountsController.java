package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.UsersDAO;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Users;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountsController {

    private AccountsDAO accountsDAO;
    private UsersDAO usersDAO;

    public AccountsController(AccountsDAO accountsDAO, UsersDAO usersDAO) {
        this.accountsDAO = accountsDAO;
        this.usersDAO = usersDAO;
    }

    @RequestMapping(path = "accounts", method = RequestMethod.GET)
    public Accounts displayAccount(Principal principal) {
        return accountsDAO.getsAccountsByUsername(principal.getName());
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

}
