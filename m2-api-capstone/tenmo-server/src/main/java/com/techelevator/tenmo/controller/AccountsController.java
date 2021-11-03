package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountsDAO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountsController {

    private AccountsDAO accountsDAO;
//    private final String baseURL = "http://localhost:8080/";

    public AccountsController(AccountsDAO accountsDAO) {
        this.accountsDAO = accountsDAO;
    }

    @RequestMapping(path = "accounts", method = RequestMethod.GET)
    public double getAccountBalance(Principal principal) {
        return accountsDAO.returnAccountBalance(principal.getName());
    }

}
