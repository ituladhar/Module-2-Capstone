package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.UserCredentials;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Scanner;
import java.io.Console;

public class ConsoleService  {

    // Define color constants
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("\t\t" + TEXT_PURPLE + "╔═══════════════════════════════════════════╗" + TEXT_RESET);
        System.out.println("\t\t" + TEXT_PURPLE + "║    ««««««««««««««««»»»»»»»»»»»»»»»»»»     ║" + TEXT_RESET);
        System.out.println("\t\t" + TEXT_PURPLE + "║"+"\t\t\t*" + TEXT_RESET +" Welcome to "+ TEXT_RED +"T"+ TEXT_BLUE +"E" + TEXT_RESET + "nmo! "+TEXT_PURPLE+"*\t\t\t║" + TEXT_RESET);
        System.out.println("\t\t" + TEXT_PURPLE + "║    ««««««««««««««««»»»»»»»»»»»»»»»»»»     ║" + TEXT_RESET);
        System.out.println("\t\t" + TEXT_PURPLE + "╚═══════════════════════════════════════════╝" + TEXT_RESET);

    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("\t\t\t\t1: "+ TEXT_BLUE +"Register" + TEXT_RESET);
        System.out.println( "\t\t\t\t2: "+ TEXT_GREEN +"Login" + TEXT_RESET);
        System.out.println("\t\t\t\t0: " +TEXT_RED + "Exit"+ TEXT_RESET);
        System.out.println();


    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("\t\t1: "+ TEXT_BLUE +" View your current balance"+ TEXT_RESET );
        System.out.println("\t\t2: "+ TEXT_PURPLE +" View your past transfers"+ TEXT_RESET );
        System.out.println("\t\t3: "+ TEXT_CYAN +" View your pending requests"+ TEXT_RESET );
        System.out.println("\t\t4: "+ TEXT_GREEN +" Send TE bucks"+ TEXT_RESET );
        System.out.println("\t\t5: "+ TEXT_YELLOW +" Request TE bucks"+ TEXT_RESET );
        System.out.println("\t\t0: "+ TEXT_RED +" Exit"+ TEXT_RESET );
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        System.out.println("\t\t\t\t" + TEXT_PURPLE + "══════════════════════════════════════════" + TEXT_RESET);
        String username = promptForString(TEXT_YELLOW + "\t\t\t\t\t\tUsername: ");
        String password = promptForString("\t\t\t\t\t\tPassword: " + TEXT_RESET);
        System.out.println("\t\t\t\t" + TEXT_PURPLE + "═══════════════════════════════════════════" + TEXT_RESET);
        // char[] passString = Console.readPassword();
        //    String pass = new String(passString );
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\t\tPlease enter a number »»");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\t\tPlease enter a decimal number »»");
            }
        }
    }

    public void pause() {
        System.out.println("\n\t\t«««« Press Enter to continue »»»»");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        border();
        System.out.println("\t\t««««" + TEXT_RED + "An error occurred. Check the log for details." + TEXT_RESET +" »»»»");
    }

    public void border(){
        System.out.println(TEXT_PURPLE + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + TEXT_RESET);
           }

    public void printPendingMenu(){
        System.out.println();
        System.out.println("\t\t\t\t1: "+ TEXT_GREEN + "Approve" + TEXT_RESET);
        System.out.println("\t\t\t\t2: "+ TEXT_RED + "Reject" + TEXT_RESET);
        System.out.println("\t\t\t\t0: Don't approve or reject\n\n");
        border();
        System.out.println();
    }

    public void viewTransferHistoryMenu() {
        System.out.println(TEXT_RED + "\t\t\t    ««««««««««««««««»»»»»»»»»»»»»»»»»»     " + TEXT_RESET);
        System.out.println("\t\t «««««««««««««««« Transfer »»»»»»»»»»»»»»»»»» " + TEXT_RESET);
        System.out.println(TEXT_BLUE + "\t\t\t    ««««««««««««««««»»»»»»»»»»»»»»»»»»     " + TEXT_RESET);
        System.out.println(TEXT_CYAN + "\t\t\tID" + TEXT_GREEN + "\t\t\t\tFrom "+ TEXT_CYAN +"/ "+TEXT_RED+"To" + TEXT_CYAN+"\t\t\t\tAmount" +TEXT_RESET);
    }

    public void viewPendingRequestsMenu(){
        System.out.println(TEXT_RED + "\t\t\t    ««««««««««««««««»»»»»»»»»»»»»»»»»»     " + TEXT_RESET);
        System.out.println("\t\t «««««««««««««««« Pending Transfers »»»»»»»»»»»»»»»»»» " );
        System.out.println(TEXT_BLUE + "\t\t\t    ««««««««««««««««»»»»»»»»»»»»»»»»»»     " + TEXT_RESET);
               System.out.println(TEXT_CYAN +"\t\t\t\tID \t\t\t\t\tTo \t\t\t\t Amount" + TEXT_RESET);
    }

//    public void viewPendingOptionsMenu(){
//
//    }
}
