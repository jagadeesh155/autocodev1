package com.epam.training.food.service;

import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultFoodDeliveryServiceTest {
    private DefaultFoodDeliveryService service;
    private FileDataStore dataStore;
    
    @BeforeEach
    public void setUp() {
        dataStore = new FileDataStore();
        service = new DefaultFoodDeliveryService(dataStore);
        
        Credentials creds = new Credentials("test", "test123");
        Customer customer = new Customer("Test User", "test@example.com", 50.0, creds);
        service.registerCustomer(customer);
        
        Food food = new Food("Burger", 9.99, "Tasty burger");
        service.addToMenu(food);
    }
    
    @Test
    public void testSuccessfulAuthentication() {
        Customer customer = service.authenticate("test@example.com", "test123");
        assertNotNull(customer);
        assertEquals("Test User", customer.getName());
    }
    
    @Test
    public void testFailedAuthentication() {
        assertThrows(AuthenticationException.class, () -> {
            service.authenticate("test@example.com", "wrongpass");
        });
    }
    
    @Test
    public void testPlaceOrderWithSufficientBalance() {
        Customer customer = service.authenticate("test@example.com", "test123");
        Cart cart = new Cart();
        cart.addItem(new OrderItem(service.findFoodByName("Burger"), 2));
        
        assertDoesNotThrow(() -> service.placeOrder(customer, cart));
    }
    
    @Test
    public void testPlaceOrderWithInsufficientBalance() {
        Customer customer = service.authenticate("test@example.com", "test123");
        Cart cart = new Cart();
        cart.addItem(new OrderItem(service.findFoodByName("Burger"), 10));
        
        assertThrows(LowBalanceException.class, () -> service.placeOrder(customer, cart));
    }
}