package org.unifibank.services;

import java.util.HashMap;
import java.util.Map;

import org.unifibank.model.Account;
import org.unifibank.model.User;

public class BankService {
    private Map<String, User> users = new HashMap<>();
    private static final String DATA_FILE = "users.json";

    public BankService() {
        loadUsersFromFile();
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

    public User login(String username, String password) [
        if (username == null || password == null) {
            System.out.println("Invalid input");
            return  null;
        }

        User user = users.get(username);
    ]
}
