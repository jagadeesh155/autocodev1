package com.epam.training.food.app;

import com.epam.training.food.service.FoodService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationTest {
    @Test
    void testService() {
        FoodService service = new FoodService();
        assertEquals("Serving: Sample Data", service.serveFood());
    }
}
