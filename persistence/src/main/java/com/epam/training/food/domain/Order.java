package com.epam.training.food.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Customer customer;
    private final List<OrderItem> items;
    private String status;

    public Order(Customer customer, List<OrderItem> items) {
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.status = "NEW";
    }

    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}