package org.unifibank.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.unifibank.model.Account;
import org.unifibank.model.TransactionType;
import org.unifibank.model.User;

public class BankService {
    private Map<String, User> users = new HashMap<>();
    private static final String DATA_FILE = "users.json";
    private boolean isTestMode = false;

    public BankService() {
        loadUsersFromFile();
    }

    // New constructor for test mode
    public BankService(boolean isTestMode) {
        this.isTestMode = isTestMode;
        if (!isTestMode) {
            loadUsersFromFile();
        }
    }

    public User signUp(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Username and passowrd cannot be empty");
            return null;
        }

        if (users.containsKey(username)) {
            System.out.println("Username already exists");
            return null;
        }

        // Hash password later
        User user = new User(username, password);
        String accountNumber = generateAccountNumber();
        user.setAccount(new Account(accountNumber));
        users.put(username, user);
        saveUserToFile();
        return user;
    }

    public User login(String username, String password) {
        if (username == null || password == null) {
            System.out.println("Invalid input");
            return null;
        }

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        System.out.println("Invalid credentials!");
        return null;
    }

    public String generateAccountNumber() {
        Random rand = new Random();

        while (true) {
            final String accountNumber = String.format("%010d", rand.nextInt(1000000000));
            boolean exists = users.values().stream()
                    .anyMatch(u -> u.getAccount().getAccountNumber().equals(accountNumber));
            if (!exists) {
                return accountNumber;
            }
        }
    }

    public void handleTransaction(User user, TransactionType type, double amount, String extraInfo) {
        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return;
        }
        // Special validation for number
        if (type == TransactionType.BUY_AIRTIME && !extraInfo.matches("\\d{11}")) {
            System.out.println("Invalid phone number. Must be 11 digits");
            return;
        }
        // Check if the balance is enough
        if (user.getAccount().getBalance().compareTo(BigDecimal.valueOf(amount)) >= 0) {
            // Deduct the money
            user.getAccount().setBalance(user.getAccount().getBalance().subtract(BigDecimal.valueOf(amount)));

            switch (type) {
                case WITHDRAW ->
                    System.out.println("Withdraw successful! New Balance: " + user.getAccount().getBalance());

                case TRANSFER ->
                    System.out.println("Transfer successful! New Balance: " + user.getAccount().getBalance());
                case PAY_BILL ->
                    System.out.println("Paid " + amount + " for " + extraInfo);
                case BUY_AIRTIME ->
                    System.out.println("Bought airtime of " + amount + " for " + extraInfo);
            }
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public void deposit(User user, double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive!");
            return;
        }
        user.getAccount().setBalance(user.getAccount().getBalance().add(BigDecimal.valueOf(amount)));
        saveUserToFile();
        System.out.println("Deposit successful! New balance: " + user.getAccount().getBalance());
        saveUserToFile();
    }

    public void withdraw(User user, double amount) {
        handleTransaction(user, TransactionType.WITHDRAW, amount, "");
        saveUserToFile();
    }

    public void transfer(User user, String billType, double amount) {
        handleTransaction(user, TransactionType.TRANSFER, amount, billType);
        saveUserToFile();
    }

    public void payBill(User user, String billType, double amount) {
        handleTransaction(user, TransactionType.PAY_BILL, amount, billType);
        saveUserToFile();
    }

    public void buyAirtime(User user, String phoneNumber, double amount) {
        handleTransaction(user, TransactionType.BUY_AIRTIME, amount, phoneNumber);
        saveUserToFile();
    }

    public void saveUserToFile() {
        if (isTestMode)
            return;
        JSONArray jsonArray = new JSONArray();
        for (User user : users.values()) {
            JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("password", user.getPassword()); // Hash in production work
            JSONObject accountJson = new JSONObject();
            accountJson.put("accountNumber", user.getAccount().getAccountNumber());
            accountJson.put("balance", user.getAccount().getBalance());
            userJson.put("account", accountJson);
            jsonArray.put(userJson);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                if (jsonString.length() > 0) {
                    JSONArray jsonArray = new JSONArray(jsonString.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userJson = jsonArray.getJSONObject(i);
                        User user = new User();
                        user.setUsername(userJson.getString("username"));
                        user.setPassword(userJson.getString("password"));
                        JSONObject accountJson = userJson.getJSONObject("account");
                        Account account = new Account();
                        account.setAccountNumber(accountJson.getString("accountNumber"));
                        account.setBalance(accountJson.getBigDecimal("balance"));
                        user.setAccount(account);
                        users.put(user.getUsername(), user);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading users: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error parsing JSON: " + e.getMessage());
            }
        }
    }

}