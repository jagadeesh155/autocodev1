package com.epam.training.food.app;

import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.*;
import com.epam.training.food.service.DefaultFoodDeliveryService;

public class FoodDeliveryApp {
    private DefaultFoodDeliveryService service;
    
    public FoodDeliveryApp() {
        this(new DefaultFoodDeliveryService(new FileDataStore()));
    }
    
    // Constructor for testing
    public FoodDeliveryApp(DefaultFoodDeliveryService service) {
        this.service = service;
    }
    
    // Setter for testing
    public void setService(DefaultFoodDeliveryService service) {
        this.service = service;
    }
    
    public String runApplicationFlow() {
        try {
            // Register a customer
            Credentials creds = new Credentials("john", "pass123");
            Customer customer = new Customer("John Doe", "john@example.com", 100.0, creds);
            service.registerCustomer(customer);
            
            // Add food to menu
            Food pizza = new Food("Pizza", 15.99, "Delicious pizza");
            service.addToMenu(pizza);
            
            // Create and place an order
            Cart cart = new Cart();
            cart.addItem(new OrderItem(pizza, 2));
            
            Customer authCustomer = service.authenticate("john@example.com", "pass123");
            service.placeOrder(authCustomer, cart);
            return "Order placed successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    // Additional testable methods
    public String runApplicationFlowWithInvalidCredentials() {
        try {
            // Same flow but with wrong password
            Credentials creds = new Credentials("john", "wrongpass");
            Customer customer = new Customer("John Doe", "john@example.com", 100.0, creds);
            service.registerCustomer(customer);
            
            Food pizza = new Food("Pizza", 15.99, "Delicious pizza");
            service.addToMenu(pizza);
            
            Cart cart = new Cart();
            cart.addItem(new OrderItem(pizza, 2));
            
            service.authenticate("john@example.com", "wrongpass");
            return "Unexpected success";
        } catch (Exception e) {
            return "Authentication failed: " + e.getMessage();
        }
    }
    
    public String runApplicationFlowWithLowBalance() {
        try {
            Credentials creds = new Credentials("john", "pass123");
            Customer customer = new Customer("John Doe", "john@example.com", 5.0, creds);
            service.registerCustomer(customer);
            
            Food pizza = new Food("Pizza", 15.99, "Delicious pizza");
            service.addToMenu(pizza);
            
            Cart cart = new Cart();
            cart.addItem(new OrderItem(pizza, 1)); // Should still fail
            
            Customer authCustomer = service.authenticate("john@example.com", "pass123");
            service.placeOrder(authCustomer, cart);
            return "Unexpected success";
        } catch (Exception e) {
            return "Payment failed: " + e.getMessage();
        }
    }
    
    public static void main(String[] args) {
        FoodDeliveryApp app = new FoodDeliveryApp();
        System.out.println(app.runApplicationFlow());
    }
}