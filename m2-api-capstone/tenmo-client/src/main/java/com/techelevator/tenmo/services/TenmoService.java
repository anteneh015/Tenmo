package com.techelevator.tenmo.services;

import com.techelevator.tenmo.auth.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Accounts;
import com.techelevator.tenmo.models.Transfers;
import io.cucumber.java.bs.A;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TenmoService {

    private RestTemplate restTemplate = new RestTemplate();
    private String baseUrl;
    private AuthenticatedUser currentUser;

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
        HttpHeaders headers = makeAuthHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Accounts> entity = new HttpEntity(newTransfers, headers);
        String url = this.baseUrl + "transfers";
        try {
            newTransfers = restTemplate.postForObject(url, entity, Transfers.class);
        } catch (RestClientResponseException ex) {
        } return newTransfers;
    }

    public Accounts getAccountFromUsername(String username) {
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        Accounts accounts = null;
        String url = this.baseUrl + "accounts?username=" + username;
        try {
            Accounts[] accountsArray = restTemplate.exchange(url, HttpMethod.GET, entity, Accounts[].class).getBody();
            accounts = accountsArray[0];
            accounts.setOwnerUsername(username);
        } catch (RestClientResponseException ex) {
        }
        return accounts;
    }

    public int getAccountIdFromUsername(String username) {
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        String url = this.baseUrl + "users/" + username + "/accountId";
        int accountId = 0;
        try {
            accountId = restTemplate.exchange(url, HttpMethod.GET, entity, int.class).getBody();
        } catch (RestClientResponseException ex) {

        }
        return accountId;
    }

    public Accounts addToBalance (Accounts account, double amount) {
        account.setBalance(account.getBalance() + amount);
        HttpHeaders headers = makeAuthHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Accounts> entity = new HttpEntity(account, headers);

        String url = this.baseUrl + "accounts/" + account.getAccountId();
        try{
            restTemplate.put(url, entity);
        } catch (RestClientResponseException ex) {
        }
        return account;
    }

    public Accounts subtractFromBalance (Accounts account, double amount) {
        account.setBalance(account.getBalance() - amount);
        HttpHeaders headers = makeAuthHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Accounts> entity = new HttpEntity(account, headers);

        String url = this.baseUrl + "accounts/" + account.getAccountId();
        try{
            restTemplate.put(url, entity);
        } catch (RestClientResponseException ex) {
        }
        return account;
    }

    public List<Transfers> getTransfersFromAccountId(int id) {
        HttpEntity entity = new HttpEntity<>(makeAuthHeader());
        Transfers[] transfersFirst = null;
        String urlOne = this.baseUrl + "transfers?accountFrom=" + id;
        transfersFirst = restTemplate.exchange(urlOne, HttpMethod.GET, entity, Transfers[].class).getBody();
        Transfers[] transfersSecond = null;
        String urlTwo = this.baseUrl + "transfers?accountTo=" + id;
        transfersSecond = restTemplate.exchange(urlTwo, HttpMethod.GET, entity, Transfers[].class).getBody();
        List<Transfers> transfersList = new ArrayList<Transfers>();
        transfersList.addAll(Arrays.asList(transfersFirst));
        transfersList.addAll(Arrays.asList(transfersSecond));
        return transfersList;
    }

    private HttpHeaders makeAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return headers;
    }


}
