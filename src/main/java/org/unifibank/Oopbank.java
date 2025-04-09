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
        boolean running = true;

        while (running) {
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
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }

            if (currentUser != null) {
                System.out.println("\nAccount Number: " + currentUser.getAccount().getAccountNumber());
                System.out.println("Balance " + currentUser.getAccount().getBalance());
                System.out.println("1. Transfer | 2. Deposit | 3. Withdraw | 4. Pay Bill | 5. Buy Airtime | 6. Logout");
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
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.deposit(currentUser, amount);
                    }
                    case 3 -> {
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.withdraw(currentUser, amount);
                    }
                    case 4 -> {
                        System.out.print("Enter bill type (e.g., electricity): ");
                        String billType = scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.payBill(currentUser, billType, amount);
                    }
                    case 5 -> {
                        System.out.print("Enter phone number: ");
                        String phoneNumber = scanner.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        bankService.buyAirtime(currentUser, phoneNumber, amount);
                    }
                    case 6 -> {
                        currentUser = null;
                    }

                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
        scanner.close();
    }
}
