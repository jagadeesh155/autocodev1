package com.epam.training.food.domain;

public class Customer {
    private final String name;
    private final String email;
    private double balance;
    private final Credentials credentials;

    public Customer(String name, String email, double balance, Credentials credentials) {
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.credentials = credentials;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public Credentials getCredentials() { return credentials; }
    public void setBalance(double balance) { this.balance = balance; }
}