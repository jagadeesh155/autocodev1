package com.epam.training.food.data;

import com.epam.training.food.domain.*;
import java.util.*;

public class FileDataStore {
    private final Map<String, Customer> customers = new HashMap<>();
    private final Map<String, Food> menu = new HashMap<>();
    private final List<Order> orders = new ArrayList<>();

    public void addCustomer(Customer customer) {
        customers.put(customer.getEmail(), customer);
    }

    public Customer findCustomerByEmail(String email) {
        return customers.get(email);
    }

    public void addFoodItem(Food food) {
        menu.put(food.getName(), food);
    }

    public Food findFoodByName(String name) {
        return menu.get(name);
    }

    public void placeOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getCustomerOrders(String email) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomer().getEmail().equals(email)) {
                result.add(order);
            }
        }
        return result;
    }
}