package com.epam.training.food.app;

import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.*;
import com.epam.training.food.service.DefaultFoodDeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FoodDeliveryAppTest {
    
    @Mock
    private FileDataStore mockDataStore;
    
    @Mock
    private DefaultFoodDeliveryService mockService;
    
    private FoodDeliveryApp app;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        app = new FoodDeliveryApp();
    }
    
    @Test
    public void testMainApplicationFlow_Success() {
        // Setup test data
        Credentials creds = new Credentials("john", "pass123");
        Customer customer = new Customer("John Doe", "john@example.com", 100.0, creds);
        Food pizza = new Food("Pizza", 15.99, "Delicious pizza");
        
        // Mock service behavior
        when(mockService.authenticate("john@example.com", "pass123")).thenReturn(customer);
        when(mockService.findFoodByName("Pizza")).thenReturn(pizza);
        
        // Inject mock service into app (would need setter or reflection in real app)
        // This is simplified for testing purposes
        app.setService(mockService);
        
        // Execute test
        String result = app.runApplicationFlow();
        
        // Verify results
        assertEquals("Order placed successfully!", result);
        verify(mockService).registerCustomer(any(Customer.class));
        verify(mockService).addToMenu(any(Food.class));
        verify(mockService).authenticate("john@example.com", "pass123");
        verify(mockService).placeOrder(eq(customer), any());
    }
    
    @Test
    public void testMainApplicationFlow_AuthenticationFailure() {
        // Mock service to throw authentication exception
        when(mockService.authenticate("john@example.com", "wrongpass"))
            .thenThrow(new com.epam.training.food.service.AuthenticationException("Invalid credentials"));
        
        app.setService(mockService);
        
        String result = app.runApplicationFlowWithInvalidCredentials();
        
        assertEquals("Authentication failed: Invalid credentials", result);
    }
    
    @Test
    public void testMainApplicationFlow_LowBalance() {
        Credentials creds = new Credentials("john", "pass123");
        Customer customer = new Customer("John Doe", "john@example.com", 5.0, creds);
        
        when(mockService.authenticate("john@example.com", "pass123")).thenReturn(customer);
        when(mockService.findFoodByName("Pizza")).thenReturn(new Food("Pizza", 15.99, "Delicious pizza"));
        
        doThrow(new com.epam.training.food.service.LowBalanceException("Insufficient balance"))
            .when(mockService).placeOrder(any(), any());
        
        app.setService(mockService);
        
        String result = app.runApplicationFlowWithLowBalance();
        
        assertEquals("Payment failed: Insufficient balance", result);
    }
}