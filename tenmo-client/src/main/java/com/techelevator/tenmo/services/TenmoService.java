package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TenmoService {

    public static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getAccountBalance() {
        BigDecimal balance = new BigDecimal(0);
        try {
            balance = restTemplate.exchange(API_BASE_URL + "balance/",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    BigDecimal.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Account getAccountById(long userId) {
        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "account/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error in retrieving account");
        }
        return account;
    }

    public long getUserIdByAccountId(long accountId){
        long userId = 0;
        try {
            userId = restTemplate.exchange(API_BASE_URL + "user/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Long.class).getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error!User ID not found");
        }
        return userId;
    }

    public User[] getAllUsers() {
        User[] listOfUsers = null;
        try {
            listOfUsers = restTemplate.exchange(
                    API_BASE_URL + "users",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error! User is not found");
        }
        return listOfUsers;
    }

    public Transfer[] getAllTransfers() {
        Transfer[] listOfTransfers = null;
        try {
            listOfTransfers = restTemplate.exchange(
                    API_BASE_URL + "transfers",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class).getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error! Approved transfers not found");
        }
        return listOfTransfers;
    }

    public Transfer[] getAllPendingTransfers() {
        Transfer[] listOfTransfers = null;
        try {
            listOfTransfers = restTemplate.exchange(
                    API_BASE_URL + "transfers/pending",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error! Pending transfers not found");
        }
        return listOfTransfers;
    }

    public Transfer getTransferById(long transferId){
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(
                    API_BASE_URL + "transfers/" + transferId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error! Transfer is not found");
        }
        return transfer;
    }

    public Transfer makeTransfer(long userToId, BigDecimal amount) {
        TransferDTO transferDTO = new TransferDTO(userToId, amount);
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "transfers",
                    HttpMethod.POST,
                    makeTransferEntity(transferDTO),
                    Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
         e.printStackTrace();
            System.out.println("Error! Unable to make a transfer");
        }
        return transfer;
    }

    public Transfer makeRequest(long userFromId, BigDecimal amount) {
        TransferDTO transferDTO = new TransferDTO(userFromId, amount);
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "requests",
                    HttpMethod.POST,
                    makeTransferEntity(transferDTO),
                    Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error in making a request");
        }
        return transfer;
    }
    public boolean acceptRequest(long transferId, long accountToId, BigDecimal amount){
        TransferDTO transferDTO = new TransferDTO(accountToId, amount);
        try {
            restTemplate.put(API_BASE_URL + "transfer/" + transferId + "/accept",
                    makeTransferEntity(transferDTO));
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error in accepting request");
        } return false;

    }


    public boolean rejectRequest(long transferId, long accountToId, BigDecimal amount){
        TransferDTO transferDTO = new TransferDTO(accountToId, amount);
        try {
            restTemplate.put(API_BASE_URL + "transfer/" + transferId + "/reject",
                    makeTransferEntity(transferDTO));
            return true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error in rejecting request");
        } return false;
    }


    public String username(long accountId) {
        String username = null;
        try {
            username = restTemplate.exchange(API_BASE_URL + "username/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    String.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error in retrieving username");
        }
        return username;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    // Creates a new HttpEntity with the `Authorization: Bearer:`
    // header and a reservation request body
        private HttpEntity<TransferDTO> makeTransferEntity(TransferDTO transferDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        HttpEntity<TransferDTO> entity = new HttpEntity<>(transferDTO, headers);
        return entity;
    }

}
