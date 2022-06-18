package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import com.techelevator.tenmo.services.TenmoService;

import java.util.Scanner;
import java.math.BigDecimal;

public class App {


    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private final TenmoService tenmoService = new TenmoService();
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        tenmoService.setAuthToken(currentUser.getToken());
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {
        // Your current account balance is: $9999.99
        // TODO Auto-generated method stub
        System.out.println("Your current balance is: $" + tenmoService.getAccountBalance());
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub

        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID \t\t\t From/To \t\t\t   Amount");
        System.out.println("-------------------------------------------");
        tenmoService.getAllTransfers();

//        tenmoService.getAccountById(currentUser.getUser().getId());
////        tenmoService.getAccountById(currentUser.getUser().getId());

//        Transfer[] listOfTransfers = tenmoService.getAllTransfers();
//        long currentAccountId = tenmoService.getAccountById(currentUser.getUser().getId()).getAccountId();
//        for (Transfer transfer : listOfTransfers) {
//            String usernameTo = tenmoService.username(transfer.getAccountToUsername());
//            String usernameFrom = tenmoService.username(transfer.getAccountFromUsername());
//            if (transfer.getAccountFromUsername() == currentAccountId) {
//                System.out.println(transfer.getTransferId() + "To: " + usernameTo + transfer.getAmount());
//            } else if (transfer.getAccountToUsername() == currentAccountId) {
//                System.out.println(transfer.getTransferId() + "From: " + usernameFrom + transfer.getAmount());
//            }
//        }
        boolean viewHistory = true;
        while (viewHistory) {
            Transfer[] listOfTransfers = tenmoService.getAllTransfers();
            consoleService.border();
            System.out.println("Transfer");
            System.out.println("ID \t\tFrom/To \tAmount");
            consoleService.border();
            long currentAccountId = tenmoService.getAccountById(currentUser.getUser().getId()).getAccountId();
            for (Transfer transfer : listOfTransfers) {
                String usernameTo = tenmoService.username(transfer.getAccountTo());
                String usernameFrom = tenmoService.username(transfer.getAccountFrom());
                if (transfer.getAccountFrom() == currentAccountId) {
                    System.out.println(transfer.getTransferId() + "\tTo: " + usernameTo + "\t" + transfer.getAmount());
                } else if (transfer.getAccountTo() == currentAccountId) {
                    System.out.println(transfer.getTransferId() + "\tFrom: " + usernameFrom + "\t" + transfer.getAmount());
                }

            }
            consoleService.border();
            int input = consoleService.promptForInt("\nPlease enter transfer ID to view details (0 to cancel): ");
            if (input == 0) {
                viewHistory = false;
            } else {
                Transfer transfer = tenmoService.getTransferById(input);
                if (transfer.getTransferId() == 0) {
                    System.out.println("\nInvalid Selection. Please Try Again.");
                    consoleService.pause();
                } else {
                    consoleService.border();
                    System.out.println("Transfer Details");
                    consoleService.border();
                    System.out.println("ID:" + transfer.getTransferId());
                    System.out.println("From: " + tenmoService.username(transfer.getAccountFrom()));
                    System.out.println("To: " + tenmoService.username(transfer.getAccountTo()));
                    System.out.println("Type: " + transfer.getTransferType());
                    System.out.println("Status: " + transfer.getTransferStatus());
                    System.out.println("Amount: $" + transfer.getAmount());
                    consoleService.border();
                    consoleService.pause();
                }
            }
        }
    }

    private void viewPendingRequests() {
        boolean checkPendingRequest = true;
        while (checkPendingRequest) {
            consoleService.border();
            System.out.println("Pending Transfers");
            System.out.println("ID \t\tTo \t\t\tAmount");
            consoleService.border();

            Transfer[] pendingTransfers = tenmoService.getAllPendingTransfers();
            for (Transfer transfer : pendingTransfers) {
                String usernameTo = tenmoService.username(transfer.getAccountTo());
                System.out.println(transfer.getTransferId() + "\t" + usernameTo + "\t" + transfer.getAmount());

            }
            consoleService.border();

            long input = consoleService.promptForInt("\nPlease enter transfer ID to approve/reject (0 to cancel): ");
            if (input == 0) {
                checkPendingRequest = false;
            } else {
                Transfer transfer = tenmoService.getTransferById(input);
                if (transfer.getTransferId() == 0) {
                    System.out.println("\nInvalid Selection. Please Try Again.");
                    consoleService.pause();
                } else {
                    viewPendingOptions(input);
                }
            }
        }
    }

    //
    private void viewPendingOptions(long id) {
        Transfer transfer = tenmoService.getTransferById(id);
        BigDecimal amount = transfer.getAmount();
        long accountToId = transfer.getAccountTo();
        long userToId = tenmoService.getUserIdByAccountId(accountToId);
        System.out.println();
        consoleService.border();
        consoleService.border();
        System.out.println("ID:" + transfer.getTransferId());
        System.out.println("From: " + tenmoService.username(transfer.getAccountFrom()));
        System.out.println("To: " + tenmoService.username(transfer.getAccountTo()));
        System.out.println("Type: " + transfer.getTransferType());
        System.out.println("Status: " + transfer.getTransferStatus());
        System.out.println("Amount: $" + transfer.getAmount());
        consoleService.border();
        consoleService.border();
        boolean checkStatus = true;
        while (checkStatus) {
            consoleService.printPendingMenu();
            int input = consoleService.promptForInt("Please choose an option: ");
            if (input == 1) {
                if (tenmoService.acceptRequest(id, userToId, amount)) {
                    System.out.println("Request Accepted.");
                    checkStatus = false;
                } else {
                    System.out.println("Request Denied. Insufficient Funds.");
                    checkStatus = false;
                }
            }
            if (input == 2) {
                if (tenmoService.rejectRequest(id, userToId, amount)) {
                    System.out.println("Request Denied.");
                    checkStatus = false;
                }
            } else if (input == 0) {
                checkStatus = false;
            }
        }
    }
    //

  /*      private void viewPendingRequests () {
            // TODO Auto-generated method stub

            System.out.println("-------------------------------------------");
            System.out.println("Pending Transfers");
            System.out.println("ID \t\t\t From/To \t\t\t   Amount");
            System.out.println("-------------------------------------------");
            tenmoService.getAllPendingTransfers();

        }*/

        private void sendBucks () {
            // TODO Auto-generated method stub
            boolean checkSendMoney = true;
            Transfer transfer = null;
            while (checkSendMoney) {
                System.out.println("Users ID\t" + "Name");
                consoleService.border();
                User[] listUsers = tenmoService.getAllUsers();
                for (User user : listUsers) {
                    System.out.println(user.getId() + "\t\t" + user.getUsername());
                }
                consoleService.border();
                int id = consoleService.promptForInt("Enter the ID of user you are sending to (0 to cancel): ");
                if (id == 0) {
                    checkSendMoney = false;
                }
                else if (tenmoService.getAccountById(id) == null) {
                    System.out.println("\nInvalid Selection. Please try again.");
                    consoleService.pause();
                } else {
                    BigDecimal amount = consoleService.promptForBigDecimal("Enter amount in decimal: ");
                    transfer = tenmoService.makeTransfer(id, amount);
                    System.out.println(transfer);
                    checkSendMoney = false;
                }
            }
        }

        private void requestBucks () {
            // TODO Auto-generated method stub
            boolean checkRequest = true;
            Transfer transfer = null;
            while (checkRequest) {
                System.out.println("Users ID\t" + "Name");
                consoleService.border();
                User[] listUsers = tenmoService.getAllUsers();
                for (User user : listUsers) {
                    System.out.println(user.getId() + "\t\t" + user.getUsername());
                }
                consoleService.border();

                int id = consoleService.promptForInt("Enter the ID of user you are requesting from (0 to cancel): ");
                if (id == 0) {
                    checkRequest = false;
                }
                else if (tenmoService.getAccountById(id) == null) {
                    System.out.println("\nInvalid Selection. Please try again.");
                    consoleService.pause();
                } else {
                    BigDecimal amount = consoleService.promptForBigDecimal("Enter amount in decimal: ");
                    transfer = tenmoService.makeRequest(id, amount);
                    System.out.println(transfer);
                    checkRequest = false;
                }
            }
        }

    }


