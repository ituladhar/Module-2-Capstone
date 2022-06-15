package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TenmoService {

public static final String API_BASE_URL = "http://localhost:5432/users/";
private final RestTemplate restTemplate = new RestTemplate();
private  String authToken = null;

    public void setAuthToken ( String authToken) {

        this.authToken = authToken;
    }

    // We need this one for list all users

    public Transfer[] getAllTransfer() {
        Transfer [] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return transfers;
    }


    // List of transfer based on the id or users
    public Transfer getTransfer(int id) {
        Transfer transfer = null;
        try {

            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return transfer;
    }



    public Transfer add(Transfer newTransfer) {
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



    public boolean sendMoney(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + transfer.getTransferId(), entity);
            success =true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return success;

    }



    public boolean requestMoney(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + transfer.getTransferId(), entity);
            success =true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return success;

    }




    public Transfer [] viewPendingTransferRequests( int id) {

        Transfer [] transferPending = null ;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferPending= response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());

        }
        return transferPending;


    }


    public boolean approveOrRejectTransfer (Transfer transfer) {
        HttpEntity <Transfer> entity = makeTransferEntity(transfer);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL +transfer.getTransferId(), entity);
            success = true;
        } catch ( RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return success;

    }




    public boolean update(Transfer updatedTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(updatedTransfer);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + updatedTransfer.getTransferId(), entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return success;
    }



    public boolean delete(int TransferId) {
        boolean success = false;
        try {
            restTemplate.exchange(API_BASE_URL + TransferId, HttpMethod.DELETE, makeAuthEntity(), Void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println(e.getMessage());
        }
        return success;
    }





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





}















//
//    public static final String API_BASE_URL = "http://localhost:8080/auctions/";
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private String authToken = null;
//
//    public void setAuthToken(String authToken) {
//        this.authToken = authToken;
//    }
//
//    public Auction[] getAllAuctions() {
//        Auction[] auctions = null;
//        try {
//            ResponseEntity<Auction[]> response =
//                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), Auction[].class);
//            auctions = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//
//        }
//        return auctions;
//    }
//
//    public Auction getAuction(int id) {
//        Auction auction = null;
//        try {
//            // Add code here to send the request to the API and get the auction from the response.
//            ResponseEntity<Auction> response = restTemplate.exchange(API_BASE_URL + id, HttpMethod.GET, makeAuthEntity(), Auction.class);
//            auction = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//
//        }
//        return auction;
//    }
//
//    public Auction[] getAuctionsMatchingTitle(String title) {
//        Auction[] auctions = null;
//        try {
//            ResponseEntity<Auction[]> response =
//                    restTemplate.exchange(API_BASE_URL + "?title_like=" + title, HttpMethod.GET,
//                            makeAuthEntity(), Auction[].class);
//            auctions = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//
//        }
//        return auctions;
//    }
//
//    public Auction[] getAuctionsAtOrBelowPrice(double price) {
//        Auction[] auctions = null;
//        try {
//            ResponseEntity<Auction[]> response =
//                    restTemplate.exchange(API_BASE_URL + "?currentBid_lte=" + price, HttpMethod.GET,
//                            makeAuthEntity(), Auction[].class);
//            auctions = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//            System.out.println(e.getMessage());
//        }
//        return auctions;
//    }
//
//    public Auction add(Auction newAuction) {
//        HttpEntity<Auction> entity = makeAuctionEntity(newAuction);
//        Auction returnedAuction = null;
//        try {
//            returnedAuction = restTemplate.postForObject(API_BASE_URL, entity, Auction.class);
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//            System.out.println(e.getMessage());
//        }
//        return returnedAuction;
//    }
//
//    public boolean update(Auction updatedAuction) {
//        HttpEntity<Auction> entity = makeAuctionEntity(updatedAuction);
//        boolean success = false;
//        try {
//            restTemplate.put(API_BASE_URL + updatedAuction.getId(), entity);
//            success = true;
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//            System.out.println(e.getMessage());
//        }
//        return success;
//    }
//
//    public boolean delete(int auctionId) {
//        boolean success = false;
//        try {
//            restTemplate.exchange(API_BASE_URL + auctionId, HttpMethod.DELETE, makeAuthEntity(), Void.class);
//            success = true;
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//            System.out.println(e.getMessage());
//        }
//        return success;
//    }
//
//    private HttpEntity<Auction> makeAuctionEntity(Auction auction) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(authToken);
//        return new HttpEntity<>(auction, headers);
//    }
//
//    private HttpEntity<Void> makeAuthEntity() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authToken);
//        return new HttpEntity<>(headers);
//    }
//
//}
//
//}
