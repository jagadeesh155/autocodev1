package com.epam.training.food.data;

import com.epam.training.food.domain.Cart;
import com.epam.training.food.domain.Customer;
import com.epam.training.food.domain.Food;
import com.epam.training.food.domain.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileDataStore implements DataStore {

    private final String inputFolderPath;
    private List<Customer> customers = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private long customerIdCounter = 1;
    public FileDataStore(String inputFolderPath) {
        this.inputFolderPath=inputFolderPath;
    }

    public void init() {
        loadCustomers();
        loadFoods();
    }

    @Override
    public List<Customer> getCustomers() {
        return customers;
    }

    @Override
    public List<Food> getFoods() {
         return foods;
    }

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public Order createOrder(Order order) {
        orders.add(order);
        return order;
    }

    @Override
    public void writeOrders() {
        try (FileWriter writer = new FileWriter(inputFolderPath + "/orders.csv", true)) {
            for (Order order : orders) {
                writer.write(formatOrderForFile(order));
                writer.write(System.lineSeparator());
            }
            orders.clear(); // Clear after writing
        } catch (IOException e) {
            throw new RuntimeException("Failed to write orders", e);
        }
    }

    private String formatOrderForFile(Order order) {

        StringBuilder sb = new StringBuilder();
        sb.append(order.getCustomerId()).append(",");
        sb.append(order.getPrice()).append(",");
        sb.append(order.getTimestampCreated()).append(",");

        String foodItems = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getFood().getName())
                .collect(Collectors.joining("|"));

        sb.append(foodItems);

        return sb.toString();
    }

    private void loadCustomers() {
        File file = new File(inputFolderPath + "/customers.csv");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                String username = parts[0];
                String password = parts[1];
                String firstName = parts[2];
                String lastName = parts[3];
                BigDecimal balance = new BigDecimal(parts[4]);

                String fullName = firstName + " " + lastName;

                Customer customer = new Customer.Builder()
                        .userName(username)
                        .password(password)
                        .name(fullName)
                        .balance(balance)
                        .cart(new Cart())  // ⚡ create empty Cart
                        .id(generateId())   // ⚡ generate id (method needed)
                        .build();

                customers.add(customer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load customers", e);
        }

    }

    private long generateId() {
        return customerIdCounter++;
    }

    /* public Customer(String userName, String password, long id, String name, BigDecimal balance, Cart cart) {
         super(userName, password);
         this.id = id;
         this.name = name;
         this.balance = balance;
         this.cart = cart;
     }*/
    private void loadFoods() {
        File file = new File(inputFolderPath + "/foods.csv");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                String name = parts[0];
                BigDecimal calorie = BigDecimal.valueOf(Long.parseLong(parts[1]));
                String description = parts[2];
                BigDecimal price = new BigDecimal(parts[3]);
                foods.add(new Food(name, calorie, description, price));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load foods", e);
        }
    }

}
