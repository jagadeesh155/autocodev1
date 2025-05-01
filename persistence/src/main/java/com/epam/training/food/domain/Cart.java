package com.epam.training.food.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotal() {
        return items.stream()
                   .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                   .sum();
    }
}