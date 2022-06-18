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


    private final RestTemplate restTemplate = new RestTemplate();
    ConsoleService consoleService = new ConsoleService();


    AuthenticatedUser authenticatedUser;
    AuthenticatedUser currentUser;
    public static final String API_BASE_URL = "http://localhost:8080/";
    private String authToken = null;

//    public TenmoService(AuthenticatedUser authenticatedUser, String API_BASE_URL) {
//        this.authenticatedUser = authenticatedUser;
//        API_BASE_URL = url;
//    }


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

            long currentAccountId = getAccountById(currentUser.getUser().getId()).getAccountId();
            for (Transfer transfer : listOfTransfers) {
                String usernameTo = username(transfer.getAccountToUsername());
                String usernameFrom = username(transfer.getAccountFromUsername());
                if (transfer.getAccountFromUsername() == currentAccountId) {
                    System.out.println(transfer.getTransferId() + "To: " + usernameTo + transfer.getAmount());
                } else if (transfer.getAccountToUsername() == currentAccountId) {
                    System.out.println(transfer.getTransferId() + "From: " + usernameFrom + transfer.getAmount());
                }
            }
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


    public Transfer[] getPendingTransfers() {
        Transfer[] listOfPendingTransfers = null;
        try {
            listOfPendingTransfers = restTemplate.exchange(
                    API_BASE_URL + "transfers/pending",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class).getBody();
            long currentAccountId = getAccountById(currentUser.getUser().getId()).getAccountId();
            for (Transfer transfer : listOfPendingTransfers) {
                String usernameTo = username(transfer.getAccountToUsername());
                String usernameFrom = username(transfer.getAccountFromUsername());
                if (transfer.getAccountFromUsername() == currentAccountId) {
                    System.out.println(transfer.getTransferId() + "To: " + usernameTo + transfer.getAmount());
                } else if (transfer.getAccountToUsername() == currentAccountId) {
                    System.out.println(transfer.getTransferId() + "From: " + usernameFrom + transfer.getAmount());
                }
            }

            System.out.println("---------");
            System.out.println("Please enter transfer ID to approve/reject (0 to cancel):\"");
            int menuSelection = consoleService.promptForMenuSelection("Please make a choice :\"");
            System.out.println("1: Approve");
            System.out.println("2: Reject");
            System.out.println("0: Don't approve or reject");
            Transfer t = new Transfer();
                if (menuSelection == 1) {
                    acceptRequest(t.getTransferId(),t.getAccountToId(),t.getAmount());
                } else if ( menuSelection == 2) {
                    rejectRequest(t.getTransferId(),t.getAccountToId(),t.getAmount());
                } else {
                    consoleService.pause();
                }

        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error! Pending transfers not found");
        }

        return listOfPendingTransfers;

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


 /*   public BigDecimal getBalance( int id) {
        BigDecimal balance = null;
         try {
             ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL+id, HttpMethod.GET, makeAuthEntity(),BigDecimal.class);
                 balance = response.getBody();
         } catch (RestClientResponseException | ResourceAccessException e) {
             BasicLogger.log(e.getMessage());

         }
        return balance;

    }




    public Transfer[] transfer () {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(),Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return transfers;

    }

    public Transfer requestMoney (Transfer transfer) {

        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        Transfer returnedTransfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "?requests=" + transfer, HttpMethod.GET, makeAuthEntity(), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return transfer;
    }


    public Transfer sendMoney (Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_URL, entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return returnedTransfer;


    }


    public Transfer[] viewTransfers ( int id) {

        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return transfers;

    }


    public Transfer[] viewPendingTransferRequests (int id) {

        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "id?pending="+ id, HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return transfers;

    }

    public boolean approveOrRejectTransfer(int transferStatusId) {

        boolean success = false;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "?request="+ transferStatusId , HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return success;


    }
*/


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
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

