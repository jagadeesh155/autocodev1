package com.epam.training.food.service;

import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.*;

public class DefaultFoodDeliveryService {
    private final FileDataStore dataStore;

    public DefaultFoodDeliveryService(FileDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Customer authenticate(String email, String password) {
        Customer customer = dataStore.findCustomerByEmail(email);
        if (customer == null || !customer.getCredentials().getPassword().equals(password)) {
            throw new AuthenticationException("Invalid credentials");
        }
        return customer;
    }

    public void placeOrder(Customer customer, Cart cart) {
        double total = cart.getTotal();
        if (customer.getBalance() < total) {
            throw new LowBalanceException("Insufficient balance");
        }
        
        customer.setBalance(customer.getBalance() - total);
        Order order = new Order(customer, cart.getItems());
        dataStore.placeOrder(order);
    }

    public List<Order> getCustomerOrders(String email) {
        return dataStore.getCustomerOrders(email);
    }

    public void addToMenu(Food food) {
        dataStore.addFoodItem(food);
    }

    public Food findFoodByName(String name) {
        return dataStore.findFoodByName(name);
    }

    public void registerCustomer(Customer customer) {
        dataStore.addCustomer(customer);
    }
}