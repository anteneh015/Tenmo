package com.techelevator.tenmo.services;

import com.techelevator.tenmo.auth.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TenmoService {

    private RestTemplate restTemplate = new RestTemplate();
    private String baseUrl;
    private AuthenticatedUser currentUser;
    private ConsoleService console;

    public TenmoService(String baseUrl, AuthenticatedUser currentUser) {
        this.baseUrl = baseUrl;
        this.currentUser = currentUser;
    }

    public double getAccountBalance() {
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        String url = this.baseUrl + "accounts/balance";
        double balance = 0.0;
        try {
            balance = restTemplate.exchange(url, HttpMethod.GET, entity, double.class).getBody();
        } catch (RestClientResponseException ex) {
            console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex) {
            console.printError(ex.getMessage());
        }
        return balance;
    }

    public String[] getUsernames() {
        String[] usernameList = null;
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        String url = this.baseUrl + "users/usernames";

        usernameList = restTemplate.exchange(url, HttpMethod.GET, entity, String[].class).getBody();

        return usernameList;
    }

    public Transfers addTransfer(Transfers newTransfers) {
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        String url = this.baseUrl + "accounts/transfers";
        try {
            newTransfers = restTemplate.postForObject(url, entity, Transfers.class);
        } catch (RestClientResponseException ex) {
            console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex) {
            console.printError(ex.getMessage());
        } return newTransfers;
    }

    public int getAccountIdFromUsername(String username) {
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        String url = this.baseUrl + "users/" + username + "/accountId";
        int accountId = 0;
        try {
            accountId = restTemplate.exchange(url, HttpMethod.GET, entity, int.class).getBody();
        } catch (RestClientResponseException ex) {
            console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex) {
            console.printError(ex.getMessage());
        }
        return accountId;
    }

    public Accounts updateBalanceAccount(Accounts account, double amount) {
        account.setBalance(account.getBalance() + amount);
        HttpEntity<Accounts> entity = new HttpEntity(account, makeAuthHeader());

        String url = this.baseUrl + "accounts/" + account.getOwnerUsername();
        try{
            restTemplate.put(url, entity);
        } catch (RestClientResponseException ex) {
            console.printError(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex) {
            console.printError(ex.getMessage());
        }
        return account;
    }

    private HttpHeaders makeAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return headers;
    }


}
