package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;

public class App {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";

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
            menuSelection = consoleService.promptForMenuSelection("\t\t»» Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("\t\t«««« Invalid Selection »»»» \n");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("\t\tPlease register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            consoleService.border();
            System.out.println("\n\t«««« Registration "+ TEXT_GREEN + "successful. " + TEXT_RESET +"You can now login.»»»» \n");
            consoleService.border();
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
            menuSelection = consoleService.promptForMenuSelection("\t\t»» Please choose an option: ");
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
                System.out.println( "\t\t«««« " + TEXT_RED + "Invalid Selection."+ TEXT_RESET + "»»»» \n");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {
        consoleService.border();
        System.out.println("\t\tYour current balance is: " + TEXT_CYAN +"$" + tenmoService.getAccountBalance());
        consoleService.border();
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        boolean viewHistory = true;
        while (viewHistory) {
            Transfer[] listOfTransfers = tenmoService.getAllTransfers();
            consoleService.border();
            consoleService.viewTransferHistoryMenu();
            consoleService.border();
            long currentAccountId = tenmoService.getAccountById(currentUser.getUser().getId()).getAccountId();
            if(listOfTransfers != null)
            {
            for (Transfer transfer : listOfTransfers) {
                String usernameTo = tenmoService.username(transfer.getAccountTo());
                String usernameFrom = tenmoService.username(transfer.getAccountFrom());
                if (transfer.getAccountFrom() == currentAccountId) {
                    System.out.println("\t\t" + TEXT_BLUE + transfer.getTransferId() +
                            TEXT_RED + "\tTo:\t\t" +
                            TEXT_RESET + usernameTo + "\t" +
                            TEXT_RED + transfer.getAmount());
                } else if (transfer.getAccountTo() == currentAccountId) {
                    System.out.println("\t\t" + TEXT_BLUE +transfer.getTransferId() +
                            TEXT_GREEN +"\tFrom:\t" +
                            TEXT_RESET + usernameFrom + "\t" +
                            TEXT_GREEN + transfer.getAmount());
                }
            }
            consoleService.border();}
            int input = consoleService.promptForInt("\n\t\t»» Please enter transfer ID to view details (0 to cancel): ");
            if (input == 0) {
                viewHistory = false;
            } else {
                Transfer transfer = tenmoService.getTransferById(input);
                if (transfer.getTransferId() == 0) {
                    System.out.println("\n\t\t«««« "+ TEXT_RED + "Invalid Selection. " + TEXT_RESET +"Please Try Again.»»»»");
                    consoleService.pause();
                } else {
                    consoleService.border();
                    System.out.println(TEXT_RED + "\t\t    ««««««««««««««««»»»»»»»»»»»»»»»»»»     " + TEXT_RESET);
                    System.out.println("\t\t «««««««««««««««« Transfer Details »»»»»»»»»»»»»»»»»» " );
                    System.out.println(TEXT_BLUE + "\t\t    ««««««««««««««««»»»»»»»»»»»»»»»»»»     " + TEXT_RESET);
                    consoleService.border();
                    System.out.println(TEXT_BLUE + "\t\tID:\t\t " + TEXT_RESET + transfer.getTransferId());
                    System.out.println(TEXT_GREEN + "\t\tFrom:\t " + TEXT_RESET + tenmoService.username(transfer.getAccountFrom()));
                    System.out.println(TEXT_RED + "\t\tTo:\t\t " + TEXT_RESET + tenmoService.username(transfer.getAccountTo()));
                    System.out.println(TEXT_YELLOW + "\t\tType:\t " + transfer.getTransferTypeDesc());
                    System.out.println(TEXT_CYAN + "\t\tStatus:\t " + transfer.getTransferStatusDesc());
                    System.out.println(TEXT_PURPLE + "\t\tAmount:\t "+ TEXT_RESET + "$" + TEXT_RESET + transfer.getAmount());
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
            consoleService.viewPendingRequestsMenu();
            consoleService.border();

            Transfer[] pendingTransfers = tenmoService.getAllPendingTransfers();
            for (Transfer transfer : pendingTransfers) {
                String usernameTo = tenmoService.username(transfer.getAccountTo());
                System.out.println(TEXT_BLUE+"\t\t"+transfer.getTransferId() + "\t" + TEXT_YELLOW + usernameTo + "\t" + TEXT_CYAN +transfer.getAmount());
            }
            consoleService.border();

            long input = consoleService.promptForInt("\n\t\t»» Please enter transfer ID to " + TEXT_GREEN +"APPROVE"+ TEXT_RESET +"/" + TEXT_RED + "REJECT" +  TEXT_RESET +" (0 to cancel): ");
            if (input == 0) {
                checkPendingRequest = false;
            } else {
                Transfer transfer = tenmoService.getTransferById(input);
                if (transfer.getTransferId() == 0) {
                    System.out.println("\n\t\t««««"+TEXT_RED  + "Invalid Selection. "+ TEXT_RESET+ "Please Try Again.»»»»");
                    consoleService.pause();
                } else {
                    viewPendingOptions(input);
                }
            }
        }
    }

    private void viewPendingOptions(long id) {
        Transfer transfer = tenmoService.getTransferById(id);
        BigDecimal amount = transfer.getAmount();
        long accountToId = transfer.getAccountTo();
        long userToId = tenmoService.getUserIdByAccountId(accountToId);
        System.out.println();
        consoleService.border();
        consoleService.border();
        System.out.println(TEXT_BLUE + "\t\tID:\t\t" + TEXT_RESET + transfer.getTransferId());
        System.out.println(TEXT_GREEN + "\t\tFrom:\t " + TEXT_RESET  + tenmoService.username(transfer.getAccountFrom()));
        System.out.println(TEXT_RED + "\t\tTo:\t\t " + TEXT_RESET  + tenmoService.username(transfer.getAccountTo()));
        System.out.println(TEXT_YELLOW + "\t\tType:\t " + transfer.getTransferTypeDesc());
        System.out.println(TEXT_CYAN + "\t\tStatus:\t " + transfer.getTransferStatusDesc());
        System.out.println(TEXT_PURPLE + "\t\tAmount:\t "+ TEXT_RESET +"$"  + transfer.getAmount());
        consoleService.border();
        consoleService.border();
        boolean checkStatus = true;
        while (checkStatus) {
            consoleService.printPendingMenu();
            int input = consoleService.promptForInt("\t\t»» Please choose an option: ");
            if (input == 1) {
                if (tenmoService.acceptRequest(id, userToId, amount)) {
                    System.out.println(TEXT_GREEN + "\t\t«««« Request Accepted.»»»»" + TEXT_RESET );
                    checkStatus = false;
                } else {
                    System.out.println(TEXT_RED +"\t\t«««« Request Denied. Insufficient Funds.»»»»" + TEXT_RESET);
                    checkStatus = false;
                }
            }
            if (input == 2) {
                if (tenmoService.rejectRequest(id, userToId, amount)) {
                    System.out.println(TEXT_RED + "\t\t«««« Request Denied.»»»»"+ TEXT_RESET);
                    checkStatus = false;
                }
            } else if (input == 0) {
                checkStatus = false;
            }
        }
    }


       private void sendBucks () {
           boolean checkSendMoney = true;
           Transfer transfer = null;
           while (checkSendMoney) {
                   System.out.println(TEXT_CYAN + "\t\tUsers ID\t" + TEXT_RESET + "Name");
                   consoleService.border();
                   User[] listUsers = tenmoService.getAllUsers();
                   for (User user : listUsers) {
                       System.out.println("\t\t" + TEXT_BLUE + user.getId() + TEXT_RESET + "\t\t" + user.getUsername());
                   }
                   consoleService.border();
                   int id = consoleService.promptForInt("\t\t»» Enter the ID of user you are sending to (0 to cancel): ");
                   if (id == 0) {
                       checkSendMoney = false;
                   } else if (tenmoService.getAccountById(id) == null) {
                       System.out.println("\n\t\t«««« Invalid Selection. Please try again.»»»»");
                       consoleService.pause();
                   } else {
                       BigDecimal amount = consoleService.promptForBigDecimal("\t\t»» Enter amount in decimal: ");
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
                System.out.println(TEXT_CYAN+ "\t\tUsers ID\t"+ TEXT_RESET + "Name");
                consoleService.border();
                User[] listUsers = tenmoService.getAllUsers();
                for (User user : listUsers) {
                    System.out.println("\t\t" + TEXT_BLUE + user.getId() + TEXT_RESET +"\t\t" + user.getUsername());
                }
                consoleService.border();

                int id = consoleService.promptForInt("\t\t»» Enter the ID of user you are requesting from (0 to cancel): ");
                if (id == 0) {
                    checkRequest = false;
                }
                else if (tenmoService.getAccountById(id) == null) {
                    System.out.println("\n\t\t«««« Invalid Selection. Please try again.»»»»");
                    consoleService.pause();
                } else {
                    BigDecimal amount = consoleService.promptForBigDecimal("\t\t»» Enter amount in decimal: ");
                    transfer = tenmoService.makeRequest(id, amount);
                    System.out.println(transfer);
                    checkRequest = false;
                }
            }
        }

    }


