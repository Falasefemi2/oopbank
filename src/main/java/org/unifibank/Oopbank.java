/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.unifibank;

import java.util.Scanner;

import org.unifibank.model.User;
import org.unifibank.services.BankService;

/**
 *
 * @author FEMI
 */
public class Oopbank {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankService();
        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("1. Sign Up | 2. Login | 3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        currentUser = bankService.signUp(username, password);
                        if (currentUser != null) {
                            System.out.println("Account created! Your account number is: "
                                    + currentUser.getAccount().getAccountNumber());
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        currentUser = bankService.login(username, password);
                    }
                    case 3 -> {
                        break;
                    }
                    default -> {
                        System.out.println("Invalid options");
                        scanner.close();
                    }
                }
            }

            if (currentUser != null) {
                System.out.println("\nAccount Number: " + currentUser.getAccount().getAccountNumber());
                System.out.println("Balance " + currentUser.getAccount().getBalance());
                System.out.println("1. Transfer | 2. Pay Bill | 3. Buy Airtime | 4. Logout");
                int choice2 = scanner.nextInt();
                scanner.nextLine();

                switch (choice2) {
                    case 1 -> {
                        System.out.print("Enter recipient account number: ");
                        String recipient = scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.transfer(currentUser, recipient, amount);
                    }
                    case 2 -> {
                        System.out.print("Enter bill type (e.g., electricity): ");
                        String billType = scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.payBill(currentUser, billType, amount);
                    }
                    case 3 -> {
                        System.out.print("Enter phone number: ");
                        String phoneNumber = scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.buyAirtime(currentUser, phoneNumber, amount);
                    }
                    case 4 -> {
                        currentUser = null;
                    }

                    default -> {
                        System.out.println("Invalid choice");
                        scanner.close();
                    }
                }
            }
        }
    }
}
