package com.epam.training.food.domain;

public class OrderItem {
    private final Food food;
    private final int quantity;

    public OrderItem(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public Food getFood() { return food; }
    public int getQuantity() { return quantity; }
}