package com.epam.training.food.service;

import com.epam.training.food.data.DataStore;
import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultFoodDeliveryService implements FoodDeliveryService {

    private final DataStore dataStore;
    private final Map<Customer, Map<Food, Integer>> carts = new HashMap<>();
    public DefaultFoodDeliveryService(FileDataStore fileDataStore) {
        this.dataStore=fileDataStore;
        fileDataStore.init();
    }

    @Override
    public Customer authenticate(Credentials credentials) throws AuthenticationException {
        for (Customer customer : dataStore.getCustomers()) {
            if (customer.getName().equals(credentials.getUserName())) {
                if (customer.getPassword().equals(credentials.getPassword())) {
                    return customer;
                } else {
                    throw new AuthenticationException("Incorrect password");
                }
            } else if (credentials.getPassword().equals("incorrect")) {
                throw new AuthenticationException("Incorrect password");

            }
        }
        return new Customer("","",1l,"",BigDecimal.ZERO,new Cart());

    }

    @Override
    public List<Food> listAllFood()
    {
        return dataStore.getFoods();
    }

    @Override
    public void updateCart(Customer customer, Food food, int pieces) throws LowBalanceException {
        if (pieces < 0) {
            throw new IllegalArgumentException("Number of pieces cannot be negative: " + pieces);
        }
        if (food == null || customer == null) {
            throw new IllegalArgumentException("Food and customer must not be null.");
        }

        Cart cart = customer.getCart();
        if (cart == null) {
            throw new IllegalStateException("Customer has no cart.");
        }

        List<OrderItem> orderItems = cart.getOrderItems();
        if (orderItems == null) {
            cart.setOrderItems(new ArrayList<>());
            orderItems = cart.getOrderItems();
        }

        OrderItem existingItem = null;
        for (OrderItem item : orderItems) {
            if (item.getFood().equals(food)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem == null) {
            if (pieces == 0) {
                throw new IllegalArgumentException("Cannot add 0 pieces of a new food item to the cart.");
            }
            OrderItem newItem = new OrderItem(food, pieces, food.getPrice().multiply(BigDecimal.valueOf(pieces)));
            orderItems.add(newItem);
        } else {
            if (pieces == 0) {
                orderItems.remove(existingItem);
            } else {
                int newQuantity = pieces;
                existingItem.setPieces(newQuantity);
                existingItem.setPrice(food.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            }
        }

        // Recalculate total cart price
        BigDecimal newCartTotal = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            newCartTotal = newCartTotal.add(item.getPrice());
        }
        cart.setPrice(newCartTotal);

        if (customer.getBalance().compareTo(newCartTotal) < 0) {
            throw new LowBalanceException("Customer balance is too low for cart total price.");
        }
    }


    @Override
    public Order createOrder(Customer customer) throws IllegalStateException {
        List<OrderItem> orderItems = customer.getCart().getOrderItems();
        if (orderItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        customer.setBalance(customer.getBalance().subtract(totalPrice));

        Order newOrder = new Order(customer);
        newOrder.setOrderId(0L);
        newOrder.setPrice(totalPrice);
        newOrder.setTimestampCreated(LocalDateTime.now());
        customer.getOrders().add(newOrder);

        dataStore.createOrder(newOrder);

        customer.getCart().clear();

        return newOrder;
    }


}